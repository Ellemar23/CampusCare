<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "campuscare";

// Connect to MySQL
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$action = $_REQUEST['action'] ?? '';

switch ($action) {
    case 'insert':
        $name = $_POST['name'] ?? '';
        $time = $_POST['time'] ?? '';

        if (!empty($name) && !empty($time)) {
            $stmt = $conn->prepare("INSERT INTO doctor (name, time) VALUES (?, ?)");
            $stmt->bind_param("ss", $name, $time);

            if ($stmt->execute()) {
                echo "Doctor inserted successfully. ID: " . $stmt->insert_id;
            } else {
                echo "Insert failed: " . $stmt->error;
            }

            $stmt->close();
        } else {
            echo "Name and time are required for insertion.";
        }
        break;

    case 'fetch':
        $time = $_GET['time'] ?? '';

        if (!empty($time)) {
            $stmt = $conn->prepare("SELECT id, name FROM doctor WHERE time = ?");
            $stmt->bind_param("s", $time);
        } else {
            $stmt = $conn->prepare("SELECT id, name, time FROM doctor");
        }

        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            while ($row = $result->fetch_assoc()) {
                echo "ID: " . $row['id'] . " | Name: " . $row['name'];
                if (isset($row['time'])) {
                    echo " | Time: " . $row['time'];
                }
                echo "<br>";
            }
        } else {
            echo "No doctor found.";
        }

        $stmt->close();
        break;

    case 'update':
        $id = $_POST['id'] ?? '';
        $newTime = $_POST['new_time'] ?? '';

        if (!empty($id) && !empty($newTime)) {
            $stmt = $conn->prepare("UPDATE doctor SET time = ? WHERE id = ?");
            $stmt->bind_param("si", $newTime, $id);

            if ($stmt->execute()) {
                echo "Doctor availability updated.";
            } else {
                echo "Update failed: " . $stmt->error;
            }

            $stmt->close();
        } else {
            echo "Doctor ID and new_time are required for update.";
        }
        break;

    case 'assign':
        // Use GET instead of POST for compatibility with Android request
        $preferredName = $_GET['preferred'] ?? '';
        $selectedTime = $_GET['time'] ?? '';

        if (!empty($selectedTime)) {
            if (!empty($preferredName)) {
                // Try to assign preferred doctor first
                $stmt = $conn->prepare("SELECT id, name FROM doctor WHERE name = ? AND time = ?");
                $stmt->bind_param("ss", $preferredName, $selectedTime);
                $stmt->execute();
                $result = $stmt->get_result();

                if ($row = $result->fetch_assoc()) {
                    echo json_encode([
                        "success" => true,
                        "doctor_name" => $row['name']
                    ]);
                    $stmt->close();
                    break;
                }

                $stmt->close();
            }

            // Fallback: assign any doctor available at that time
            $stmt = $conn->prepare("SELECT id, name FROM doctor WHERE time = ? LIMIT 1");
            $stmt->bind_param("s", $selectedTime);
            $stmt->execute();
            $result = $stmt->get_result();

            if ($row = $result->fetch_assoc()) {
                echo json_encode([
                    "success" => true,
                    "doctor_name" => $row['name']
                ]);
            } else {
                echo json_encode([
                    "success" => false,
                    "message" => "No doctor available at the selected time."
                ]);
            }

            $stmt->close();
        } else {
            echo json_encode([
                "success" => false,
                "message" => "selected_time is required."
            ]);
        }

        break;

    default:
        echo "Invalid or missing action. Use 'insert', 'fetch', 'update', or 'assign'.";
        break;
}

$conn->close();
?>
