<?php
header('Content-Type: application/json');

// Database connection
$host = 'localhost';
$user = 'root';
$password = '';
$dbname = 'CampusCare';

$conn = new mysqli($host, $user, $password, $dbname);
if ($conn->connect_error) {
    echo json_encode(["success" => false, "message" => "Connection failed: " . $conn->connect_error]);
    exit;
}

// Utility function to fetch input from either POST or GET
function get($key) {
    return isset($_POST[$key]) ? trim($_POST[$key]) : (isset($_GET[$key]) ? trim($_GET[$key]) : null);
}

// Determine action
$action = get('action');

switch ($action) {
    case 'add':
        $date = get('date');
        $description = get('reason');

        if (!$date || !$description) {
            echo json_encode(["success" => false, "message" => "Missing date or description"]);
            break;
        }

        $stmt = $conn->prepare("INSERT INTO holidays (date, reason) VALUES (?, ?)");

        $stmt->bind_param("ss", $date, $description);

        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Holiday added"]);
        } else {
            echo json_encode(["success" => false, "message" => "Database error: " . $stmt->error]);
        }
        break;

    case 'check':
        $date = get('date');

        if (!$date) {
            echo json_encode(["success" => false, "message" => "Missing date"]);
            break;
        }

        $stmt = $conn->prepare("SELECT * FROM holidays WHERE date = ?");
        $stmt->bind_param("s", $date);
        $stmt->execute();
        $result = $stmt->get_result();

        echo json_encode([
            "isHoliday" => $result->num_rows > 0
        ]);
        break;

    case 'delete':
        $date = get('date');

        if (!$date) {
            echo json_encode(["success" => false, "message" => "Missing date"]);
            break;
        }

        $stmt = $conn->prepare("DELETE FROM holidays WHERE date = ?");
        $stmt->bind_param("s", $date);

        if ($stmt->execute()) {
            echo json_encode(["success" => true, "message" => "Holiday deleted"]);
        } else {
            echo json_encode(["success" => false, "message" => "Database error: " . $stmt->error]);
        }
        break;

    case 'list':
        $result = $conn->query("SELECT date FROM holidays ORDER BY date ASC");
        $holidays = [];

        while ($row = $result->fetch_assoc()) {
            $holidays[] = $row['date'];
        }

        echo json_encode($holidays);
        break;

    default:
        echo json_encode(["success" => false, "message" => "Invalid or missing action"]);
        break;
}
?>
