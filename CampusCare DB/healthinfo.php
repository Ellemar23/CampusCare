<?php
header('Content-Type: application/json');

$host = 'localhost';
$user = 'root';
$password = '';
$dbname = 'CampusCare';

$conn = new mysqli($host, $user, $password, $dbname);
if ($conn->connect_error) {
    die(json_encode(["success" => false, "message" => "Connection failed: " . $conn->connect_error]));
}

function getParam($key) {
    return isset($_REQUEST[$key]) ? trim($_REQUEST[$key]) : null;
}

function logActivity($conn, $user_id, $action, $description) {
    $stmt = $conn->prepare("INSERT INTO activity_log (user_id, action, description) VALUES (?, ?, ?)");
    if (!$stmt) return; // silently fail on logging errors
    $stmt->bind_param("sss", $user_id, $action, $description);
    $stmt->execute();
    $stmt->close();
}

$action = getParam('action');
$user_id = getParam('user_id');

if (!$action || !$user_id) {
    echo json_encode(["success" => false, "message" => "Action and user_id are required."]);
    exit;
}

switch ($action) {
    case 'get':
        // 1. Fetch health info
        $stmt = $conn->prepare("SELECT blood_group, height, weight, allergies, conditions, medications, last_checkup AS stored_last_checkup, emergency_contact FROM health_info WHERE user_id = ?");
        $stmt->bind_param("s", $user_id);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $data = $result->fetch_assoc();

            // 2. Fetch last_checkup from appointments (most recent past appointment)
            $stmt2 = $conn->prepare("
                SELECT date 
                FROM appointments 
                WHERE user_id = ? AND CONCAT(date, ' ', time) <= NOW() 
                ORDER BY CONCAT(date, ' ', time) DESC 
                LIMIT 1
            ");
            $stmt2->bind_param("s", $user_id);
            $stmt2->execute();
            $result2 = $stmt2->get_result();

            if ($result2 && $result2->num_rows > 0) {
                $row2 = $result2->fetch_assoc();
                // Override or add last_checkup with the latest appointment date
                $data['last_checkup'] = $row2['date'];
            } else {
                // fallback to stored last_checkup in health_info table if no past appointments found
                $data['last_checkup'] = $data['stored_last_checkup'];
            }
            $stmt2->close();

            // Remove stored_last_checkup from output, keep just last_checkup
            unset($data['stored_last_checkup']);

            echo json_encode(["success" => true, "data" => $data]);

        } else {
            echo json_encode(["success" => false, "message" => "No health info found for user."]);
        }
        $stmt->close();
        break;

    case 'edit':
        $blood_group = getParam('blood_group');
        $height = getParam('height');
        $weight = getParam('weight');
        $allergies = getParam('allergies');
        $conditions = getParam('conditions');
        $medications = getParam('medications');
        $last_checkup = getParam('last_checkup');
        $emergency_contact = getParam('emergency_contact');

        // Check if record exists
        $checkStmt = $conn->prepare("SELECT id FROM health_info WHERE user_id = ?");
        $checkStmt->bind_param("s", $user_id);
        $checkStmt->execute();
        $checkResult = $checkStmt->get_result();

        if ($checkResult->num_rows > 0) {
            // Update existing
            $stmt = $conn->prepare("UPDATE health_info SET 
                blood_group = ?, height = ?, weight = ?, allergies = ?, conditions = ?, medications = ?, last_checkup = ?, emergency_contact = ? 
                WHERE user_id = ?");
            $stmt->bind_param("sssssssss",
                $blood_group,
                $height,
                $weight,
                $allergies,
                $conditions,
                $medications,
                $last_checkup,
                $emergency_contact,
                $user_id
            );

            if ($stmt->execute()) {
                echo json_encode(["success" => true, "message" => "Health info updated successfully."]);
                logActivity($conn, $user_id, 'update_health_info', "Updated health info");
            } else {
                echo json_encode(["success" => false, "message" => "Update failed: " . $stmt->error]);
            }
            $stmt->close();

        } else {
            // Insert new
            $stmt = $conn->prepare("INSERT INTO health_info (user_id, blood_group, height, weight, allergies, conditions, medications, last_checkup, emergency_contact) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("sssssssss",
                $user_id,
                $blood_group,
                $height,
                $weight,
                $allergies,
                $conditions,
                $medications,
                $last_checkup,
                $emergency_contact
            );

            if ($stmt->execute()) {
                echo json_encode(["success" => true, "message" => "Health info added successfully."]);
                logActivity($conn, $user_id, 'add_health_info', "Inserted new health info");
            } else {
                echo json_encode(["success" => false, "message" => "Insert failed: " . $stmt->error]);
            }
            $stmt->close();
        }
        $checkStmt->close();
        break;

    default:
        echo json_encode(["success" => false, "message" => "Invalid action"]);
}

$conn->close();
?>
