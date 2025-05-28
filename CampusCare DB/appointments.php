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
    return isset($_POST[$key]) ? trim($_POST[$key]) : (isset($_GET[$key]) ? trim($_GET[$key]) : null);
}

function logActivity($conn, $user_id, $action, $description) {
    $stmt = $conn->prepare("INSERT INTO activity_log (user_id, action, description) VALUES (?, ?, ?)");
    if (!$stmt) {
        error_log("logActivity prepare failed: " . $conn->error);
        return false;
    }
    $stmt->bind_param("iss", $user_id, $action, $description);
    $exec = $stmt->execute();
    if (!$exec) {
        error_log("logActivity execute failed: " . $stmt->error);
    }
    $stmt->close();
    return $exec;
}

$action = get('action');

switch ($action) {
    case 'add':
        $user_id = get('user_id');
        $username = get('username'); // New
        $doctor_name = get('doctor_name');
        $date = get('date');
        $time = get('time');
        $type = get('type');
        $reason = get('reason');

        if (empty($user_id) || empty($username) || empty($doctor_name) || empty($date) || empty($time) || empty($type) || empty($reason)) {
            echo json_encode(["success" => false, "message" => "All fields are required."]);
            break;
        }

        $conflict_stmt = $conn->prepare("SELECT id FROM appointments WHERE user_id = ? AND date = ? AND time = ?");
        $conflict_stmt->bind_param("iss", $user_id, $date, $time);
        $conflict_stmt->execute();
        $conflict_result = $conflict_stmt->get_result();
        if ($conflict_result && $conflict_result->num_rows > 0) {
            echo json_encode(["success" => false, "message" => "You already have an appointment at this time."]);
            $conflict_stmt->close();
            break;
        }
        $conflict_stmt->close();

        $limit_stmt = $conn->prepare("
            SELECT limit_count 
            FROM appointment_limits 
            WHERE date = ? AND time = ?
        ");
        $limit_stmt->bind_param("ss", $date, $time);
        $limit_stmt->execute();
        $limit_result = $limit_stmt->get_result();

        $max_allowed = null;
        if ($limit_result && $limit_result->num_rows > 0) {
            $limit_row = $limit_result->fetch_assoc();
            $max_allowed = $limit_row['limit_count'];
        }
        $limit_stmt->close();

        if ($max_allowed !== null) {
            $count_stmt = $conn->prepare("
                SELECT COUNT(*) as count 
                FROM appointments 
                WHERE date = ? AND time = ?
            ");
            $count_stmt->bind_param("ss", $date, $time);
            $count_stmt->execute();
            $count_result = $count_stmt->get_result();
            $count_row = $count_result->fetch_assoc();
            $current_count = $count_row['count'];
            $count_stmt->close();

            if ($current_count >= $max_allowed) {
                echo json_encode(["success" => false, "message" => "Appointment limit reached for $time on $date."]);
                break;
            }
        }

        $stmt = $conn->prepare("INSERT INTO appointments (user_id, username, doctor_name, date, time, type, reason) VALUES (?, ?, ?, ?, ?, ?, ?)");
        $stmt->bind_param("issssss", $user_id, $username, $doctor_name, $date, $time, $type, $reason);
        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Appointment booked successfully."]);
            logActivity($conn, $user_id, 'book_appointment', "Booked appointment with $doctor_name on $date at $time");
        } else {
            echo json_encode(["success" => false, "message" => "Execute failed: " . $stmt->error]);
        }
        $stmt->close();
        break;

    case 'list':
        $user_id = get('user_id');
        if (empty($user_id)) {
            echo json_encode(["success" => false, "message" => "User ID is required."]);
            break;
        }

        $stmt = $conn->prepare("SELECT id, doctor_name, date, time FROM appointments WHERE user_id = ? ORDER BY date ASC, time ASC");
        if (!$stmt) {
            echo json_encode(["success" => false, "message" => "Prepare failed: " . $conn->error]);
            break;
        }

        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $result = $stmt->get_result();
        $appointments = [];

        while ($row = $result->fetch_assoc()) {
            $appointments[] = [
                "id" => $row["id"],
                "doctor_name" => $row["doctor_name"],
                "date" => $row["date"],
                "time" => $row["time"]
            ];
        }

        echo json_encode(["success" => true, "data" => $appointments]);
        $stmt->close();
        break;

    case 'get':
        $user_id = get('user_id');
        $date = get('date');

        if (empty($user_id) || empty($date)) {
            echo json_encode(["success" => false, "message" => "Missing user_id or date."]);
            break;
        }

        $stmt = $conn->prepare("SELECT * FROM appointments WHERE user_id = ? AND date = ? LIMIT 1");
        if (!$stmt) {
            echo json_encode(["success" => false, "message" => "Prepare failed: " . $conn->error]);
            break;
        }

        $stmt->bind_param("is", $user_id, $date);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows === 1) {
            $row = $result->fetch_assoc();
            echo json_encode([
                "success" => true,
                "data" => [
                    "id" => $row["id"],
                    "doctor_name" => $row["doctor_name"],
                    "date" => $row["date"],
                    "time" => $row["time"],
                    "type" => $row["type"],
                    "reason" => $row["reason"]
                ]
            ]);
        } else {
            echo json_encode(["success" => false, "message" => "No appointment found for given date."]);
        }

        $stmt->close();
        break;

    case 'get_id':
        $user_id = get('user_id');
        $date = get('date');

        if (empty($user_id) || empty($date)) {
            echo json_encode(["success" => false, "message" => "Missing user_id or date."]);
            break;
        }

        $stmt = $conn->prepare("SELECT id FROM appointments WHERE user_id = ? AND date = ? LIMIT 1");
        if (!$stmt) {
            echo json_encode(["success" => false, "message" => "Prepare failed: " . $conn->error]);
            break;
        }

        $stmt->bind_param("is", $user_id, $date);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows === 1) {
            $row = $result->fetch_assoc();
            echo json_encode([
                "success" => true,
                "id" => $row["id"]
            ]);
        } else {
            echo json_encode(["success" => false, "message" => "No appointment found for given date."]);
        }

        $stmt->close();
        break;

    case 'delete':
        $id = get('id');
        $user_id = get('user_id'); // Needed for logging

        if (empty($id)) {
            echo json_encode(["success" => false, "message" => "Appointment ID is required."]);
            break;
        }

        $stmt = $conn->prepare("DELETE FROM appointments WHERE id = ?");
        if (!$stmt) {
            echo json_encode(["success" => false, "message" => "Prepare failed: " . $conn->error]);
            break;
        }

        $stmt->bind_param("i", $id);
        
        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Appointment deleted successfully."]);
        } else {
            echo json_encode(["success" => false, "message" => "Execute failed: " . $stmt->error]);
        }

        logActivity($conn, $user_id, 'delete_appointment', "Deleted appointment with ID $id");
        $stmt->close();
        break;

    case 'update':
        $user_id = get('user_id');
        $id = get('id');
        $doctor = get('doctor_name');
        $date = get('date');
        $time = get('time');
        $type = get('type');
        $reason = get('reason');

        if (!$id || !$doctor || !$date || !$time || !$type || !$reason) {
            echo json_encode(["success" => false, "message" => "All fields are required for update."]);
            break;
        }

        $stmt = $conn->prepare("UPDATE appointments SET doctor_name = ?, date = ?, time = ?, type = ?, reason = ? WHERE id = ?");
        $stmt->bind_param("sssssi", $doctor, $date, $time, $type, $reason, $id);

        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Appointment updated successfully."]);
        } else {
            echo json_encode(["success" => false, "message" => "Update failed: " . $stmt->error]);
        }

        logActivity($conn, $user_id, 'update_appointment', "Updated appointment with ID $id");
        $stmt->close();
        break;

    case 'next':
        $user_id = get('user_id');

        if (empty($user_id)) {
            echo json_encode(["success" => false, "message" => "User ID is required."]);
            break;
        }

        $stmt = $conn->prepare("
            SELECT doctor_name, date, time 
            FROM appointments 
            WHERE user_id = ? 
              AND date = CURDATE()
              AND STR_TO_DATE(time, '%h:%i %p') > CURTIME()
            ORDER BY STR_TO_DATE(time, '%h:%i %p') ASC
            LIMIT 1
        ");
        if (!$stmt) {
            echo json_encode(["success" => false, "message" => "Prepare failed: " . $conn->error]);
            break;
        }

        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result && $result->num_rows > 0) {
            $row = $result->fetch_assoc();
            echo json_encode([
                "success" => true,
                "data" => [
                    "doctor_name" => $row["doctor_name"],
                    "date" => $row["date"],
                    "time" => $row["time"]
                ]
            ]);
        } else {

            echo json_encode(["success" => false, "message" => "No upcoming appointments for today."]);

        }

        $stmt->close();
        break;

    case 'set_limit':
        $date = get('date');
        $time = get('time');
        $max = get('max');

        if (!$date || !$time || !$max) {
            echo json_encode(["success" => false, "message" => "Missing date, time, or max."]);
            break;
        }

        $stmt = $conn->prepare("
            INSERT INTO appointment_limits (date, time, limit_count)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE limit_count = VALUES(limit_count)
        ");
        $stmt->bind_param("ssi", $date, $time, $max);
        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Limit set successfully."]);
        } else {
            echo json_encode(["success" => false, "message" => "Failed to set limit: " . $stmt->error]);
        }
        $stmt->close();
        break;

    default:
        echo json_encode(["success" => false, "message" => "Invalid action."]);
        break;
}

$conn->close();
