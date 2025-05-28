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

$user_id = get('user_id');
if (empty($user_id)) {
    echo json_encode(["success" => false, "message" => "User ID is required."]);
    exit;
}

$activities = [];

// Fetch from appointments
$stmt1 = $conn->prepare("SELECT date, time FROM appointments WHERE user_id = ?");
$stmt1->bind_param("i", $user_id);
$stmt1->execute();
$result1 = $stmt1->get_result();
while ($row = $result1->fetch_assoc()) {
    $activities[] = [
        "date" => $row["date"],
        "time" => $row["time"],
        "type" => "appointment"
    ];
}
$stmt1->close();

// Fetch from medical_history (assuming only 'date' column, no 'time' column)
$stmt2 = $conn->prepare("SELECT date FROM medical_history WHERE user_id = ?");
$stmt2->bind_param("s", $user_id);
$stmt2->execute();
$result2 = $stmt2->get_result();
while ($row = $result2->fetch_assoc()) {
    $activities[] = [
        "date" => $row["date"],
        "time" => null,
        "type" => "medical"
    ];
}
$stmt2->close();

// Optional: Sort by date and time
usort($activities, function($a, $b) {
    $dt1 = $a['date'] . ' ' . ($a['time'] ?? '00:00:00');
    $dt2 = $b['date'] . ' ' . ($b['time'] ?? '00:00:00');
    return strtotime($dt1) - strtotime($dt2);
});

echo json_encode(["success" => true, "data" => $activities]);

$conn->close();
