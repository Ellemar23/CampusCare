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

$action = get('action');

switch ($action) {
    case 'all_appointments':
$stmt = $conn->prepare("
    SELECT id, user_id, username, doctor_name, date, time, type, reason 
    FROM appointments 
    WHERE date >= CURDATE()
    ORDER BY date ASC, STR_TO_DATE(time, '%h:%i %p') ASC
");
    if (!$stmt) {
        echo json_encode(["success" => false, "message" => "Prepare failed: " . $conn->error]);
        break;
    }

    $stmt->execute();
    $result = $stmt->get_result();
    $appointments = [];

    while ($row = $result->fetch_assoc()) {
        $appointments[] = [
            "id" => $row["id"],
            "user_id" => $row["user_id"],
            "username" => $row["username"],
            "doctor_name" => $row["doctor_name"],
            "date" => $row["date"],
            "time" => $row["time"],
            "type" => $row["type"],
            "reason" => $row["reason"]
        ];
    }

    echo json_encode(["success" => true, "data" => $appointments]);
    $stmt->close();
    break;


    default:
        echo json_encode(["success" => false, "message" => "Invalid or missing action."]);
        break;
}

$conn->close();
