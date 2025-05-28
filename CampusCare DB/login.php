<?php
$host = "localhost"; 
$user = "root"; 
$pass = ""; 
$db = "campuscare"; 

$conn = mysqli_connect($host, $user, $pass, $db);
if (!$conn) {
    echo "db_connection_error";
    exit();
}

if (isset($_POST['email']) && isset($_POST['password'])) {
    $email = trim($_POST['email']);
    $password = trim($_POST['password']);
    $action = $_POST['action'] ?? 'login';

    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        echo "invalid_email_format";
        exit();
    }

    $stmt = $conn->prepare("SELECT id, name, role, password, verified FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result && $result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $userId = $row['id'];
        $userName = $row['name'];
        $userRole = $row['role'];
        $hashedPassword = $row['password'];
        $verified = $row['verified'];

        if (!password_verify($password, $hashedPassword)) {
            echo "invalid_credentials";
            exit();
        }

        if ($verified == 0) {
            echo "account_not_verified";
            exit();
        }

        switch ($action) {
            case 'delete':
                $deleteStmt = $conn->prepare("DELETE FROM users WHERE email = ?");
                $deleteStmt->bind_param("s", $email);
                if ($deleteStmt->execute()) {
                    echo "account_deleted";
                } else {
                    echo "delete_failed";
                }
                $deleteStmt->close();
                break;

            case 'login':
            default:
                echo "login_success:$userId:$userName:$userRole";
                break;
        }

    } else {
        echo "invalid_credentials";
    }

    $stmt->close();
} else {
    echo "missing_parameters";
}

mysqli_close($conn);
?>
