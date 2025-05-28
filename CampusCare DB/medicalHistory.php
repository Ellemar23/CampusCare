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

function get($key) {
    return isset($_POST[$key]) ? trim($_POST[$key]) : null;
}

function logActivity($conn, $user_id, $action, $description) {
    $stmt = $conn->prepare("INSERT INTO activity_log (user_id, action, description) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $user_id, $action, $description);
    $stmt->execute();
    $stmt->close();
}

$action = $_GET['action'] ?? $_POST['action'] ?? '';

switch ($action) {
    case 'add':
        $user_id = get('user_id');
        if (empty($user_id)) {
            echo json_encode(["success" => false, "message" => "User ID is required."]);
            break;
        }
        $name = get('name');
        $past_diagnoses = get('past_diagnoses');
        $previous_surgeries = get('previous_surgeries');
        $chronic_conditions = get('chronic_conditions');
        $hospitalizations = get('hospitalizations');
        $allergies = get('allergies');
        $medications_history = get('medications_history');
        $vaccination_records = get('vaccination_records');
        $family_medical_history = get('family_medical_history');
        $lifestyle_factors = get('lifestyle_factors');
        $doctor_notes = get('doctor_notes');
        $lab_test_results = get('lab_test_results');

        $stmt = $conn->prepare("INSERT INTO medical_history 
            (user_id, date, name, past_diagnoses, previous_surgeries, chronic_conditions, hospitalizations, allergies, medications_history, vaccination_records, family_medical_history, lifestyle_factors, doctor_notes, lab_test_results) 
            VALUES (?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        if (!$stmt) {
            echo json_encode(["success" => false, "message" => "Prepare failed: " . $conn->error]);
            break;
        }

        $stmt->bind_param("sssssssssssss", 
            $user_id, 
            $name,
            $past_diagnoses, 
            $previous_surgeries, 
            $chronic_conditions, 
            $hospitalizations, 
            $allergies, 
            $medications_history, 
            $vaccination_records, 
            $family_medical_history, 
            $lifestyle_factors, 
            $doctor_notes, 
            $lab_test_results
        );

        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Medical history added successfully."]);
            logActivity($conn, $user_id, 'add_medical_history', "Added medical history record for $name");
        } else {
            echo json_encode(["success" => false, "message" => "Execute failed: " . $stmt->error]);
        }

        logActivity($conn, $user_id, 'add_medical_history', "Added medical history record for $name");
        $stmt->close();
        break;

    case 'list':
        $user_id = get('user_id');
        if (empty($user_id)) {
            echo json_encode(["success" => false, "message" => "User ID is required."]);
            break;
        }
        $stmt = $conn->prepare("SELECT date, name FROM medical_history WHERE user_id = ? ORDER BY date DESC");
        if (!$stmt) {
            echo json_encode(["success" => false, "message" => "Prepare failed: " . $conn->error]);
            break;
        }
        $stmt->bind_param("s", $user_id);
        $stmt->execute();
        $result = $stmt->get_result();

        $entries = [];
        while ($row = $result->fetch_assoc()) {
            $entries[] = [
                "date" => $row["date"],
                "name" => $row["name"]
            ];
        }
        echo json_encode(["success" => true, "data" => $entries]);
        $stmt->close();
        break;

    case 'get':
        $user_id = get('user_id');
        $date = get('date');

        if (empty($user_id) || empty($date)) {
            echo json_encode(["success" => false, "message" => "User ID and date are required."]);
            break;
        }

        $stmt = $conn->prepare("SELECT * FROM medical_history WHERE user_id = ? AND date = ?");
        if (!$stmt) {
            echo json_encode(["success" => false, "message" => "Prepare failed: " . $conn->error]);
            break;
        }
        $stmt->bind_param("ss", $user_id, $date);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows === 1) {
            $row = $result->fetch_assoc();
            echo json_encode([
                "success" => true,
                "data" => [
                    "name" => $row["name"],
                    "past_diagnoses" => $row["past_diagnoses"],
                    "previous_surgeries" => $row["previous_surgeries"],
                    "chronic_conditions" => $row["chronic_conditions"],
                    "hospitalizations" => $row["hospitalizations"],
                    "allergies" => $row["allergies"],
                    "medications_history" => $row["medications_history"],
                    "vaccination_records" => $row["vaccination_records"],
                    "family_medical_history" => $row["family_medical_history"],
                    "lifestyle_factors" => $row["lifestyle_factors"],
                    "doctor_notes" => $row["doctor_notes"],
                    "lab_test_results" => $row["lab_test_results"]
                ]
            ]);
        } else {
            echo json_encode(["success" => false, "message" => "Record not found."]);
        }
        $stmt->close();
        break;

    case 'update':
        $user_id = get('user_id');
        $date = get('date');
        if (empty($user_id) || empty($date)) {
            echo json_encode(["success" => false, "message" => "User ID and date are required."]);
            break;
        }

        $name = get('name');
        $past_diagnoses = get('past_diagnoses');
        $previous_surgeries = get('previous_surgeries');
        $chronic_conditions = get('chronic_conditions');
        $hospitalizations = get('hospitalizations');
        $allergies = get('allergies');
        $medications_history = get('medications_history');
        $vaccination_records = get('vaccination_records');
        $family_medical_history = get('family_medical_history');
        $lifestyle_factors = get('lifestyle_factors');
        $doctor_notes = get('doctor_notes');
        $lab_test_results = get('lab_test_results');

        $stmt = $conn->prepare("UPDATE medical_history SET 
            name = ?, past_diagnoses = ?, previous_surgeries = ?, chronic_conditions = ?, hospitalizations = ?, allergies = ?, medications_history = ?, vaccination_records = ?, family_medical_history = ?, lifestyle_factors = ?, doctor_notes = ?, lab_test_results = ? 
            WHERE user_id = ? AND date = ?");

        if (!$stmt) {
            echo json_encode(["success" => false, "message" => "Prepare failed: " . $conn->error]);
            break;
        }

        if (!$stmt->bind_param("ssssssssssssss", 
            $name,
            $past_diagnoses, 
            $previous_surgeries, 
            $chronic_conditions, 
            $hospitalizations, 
            $allergies, 
            $medications_history, 
            $vaccination_records, 
            $family_medical_history, 
            $lifestyle_factors, 
            $doctor_notes, 
            $lab_test_results,
            $user_id, 
            $date
        )) {
            echo json_encode(["success" => false, "message" => "Bind param failed: " . $stmt->error]);
            break;
        }

        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Medical history updated successfully."]);
            logActivity($conn, $user_id, 'update_medical_history', "Updated medical history record for $name on $date");
        } else {
            echo json_encode(["success" => false, "message" => "Execute failed: " . $stmt->error]);
        }
        logActivity($conn, $user_id, 'update_medical_history', "Updated medical history record for $name on $date");
        $stmt->close();
        break;

    case 'delete':
        $user_id = get('user_id');
        $date = get('date');

        if (empty($user_id) || empty($date)) {
            echo json_encode(["success" => false, "message" => "User ID and date are required."]);
            break;
        }

        $stmt = $conn->prepare("DELETE FROM medical_history WHERE user_id = ? AND date = ?");
        if (!$stmt) {
            echo json_encode(["success" => false, "message" => "Prepare failed: " . $conn->error]);
            break;
        }
        $stmt->bind_param("ss", $user_id, $date);

        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Medical history deleted successfully."]);
            logActivity($conn, $user_id, 'delete_medical_history', "Deleted medical history record dated $date");
        } else {
            echo json_encode(["success" => false, "message" => "Execute failed: " . $stmt->error]);
        }
        logActivity($conn, $user_id, 'delete_medical_history', "Deleted medical history record dated $date");
        $stmt->close();
        break;

    default:
        echo json_encode(["success" => false, "message" => "Invalid action."]);
        break;
}

$conn->close();
?>
