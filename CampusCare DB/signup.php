<?php
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

require 'vendor/autoload.php';

$host = "localhost";
$user = "root";
$pass = "";
$db = "campuscare";

$conn = mysqli_connect($host, $user, $pass, $db);
if (!$conn) {
    die("db_connection_error");
}

$role = $_POST['role'] ?? '';
$name = $_POST['name'] ?? '';
$gender = $_POST['gender'] ?? '';
$age = $_POST['age'] ?? '';
$contact = $_POST['contact'] ?? '';
$email = $_POST['email'] ?? '';
$password = $_POST['password'] ?? '';
$confirmPassword = $_POST['confirmPassword'] ?? '';

if (empty($role) || empty($name) || empty($gender) || empty($age) || empty($contact) || empty($email) || empty($password) || empty($confirmPassword)) {
    echo "Please fill all fields";
    exit();
}

if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    echo "Invalid email format";
    exit();
}

if ($password !== $confirmPassword) {
    echo "password_mismatch";
    exit();
}

// Check if email already exists in users table
$query = "SELECT * FROM users WHERE email=?";
$stmt = $conn->prepare($query);
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

if ($result && $result->num_rows > 0) {
    $user = $result->fetch_assoc();
    if ($user['verified'] == 1) {
        echo "email_exists";
        exit();
    } else {
        // User exists but not verified: Resend OTP
        $userId = $user['id'];
        $userName = $user['name'];
        $userRole = $user['role'];

        $otp = rand(100000, 999999);
        $expiry = date("Y-m-d H:i:s", time() + 300);

        $stmtOtp = $conn->prepare("
            INSERT INTO otp_codes (email, otp, expiry_time)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE otp = VALUES(otp), expiry_time = VALUES(expiry_time)
        ");
        $stmtOtp->bind_param("sss", $email, $otp, $expiry);
        $stmtOtp->execute();

        $mail = new PHPMailer(true);
        try {
            $mail->isSMTP();
            $mail->Host       = 'smtp.gmail.com';
            $mail->SMTPAuth   = true;
            $mail->Username   = 'campuscareapp@gmail.com';
            $mail->Password   = 'qcmx nybd rmuy jnby';
            $mail->SMTPSecure = 'tls';
            $mail->Port       = 587;

            $mail->setFrom('campuscareapp@gmail.com', 'CampusCare');
            $mail->addAddress($email);
            $mail->isHTML(true);
            $mail->Subject = 'Your CampusCare OTP';
            $mail->Body    = "<p>Your OTP is: <strong>$otp</strong></p><p>It will expire in 5 minutes.</p>";

            $mail->send();
            echo "otp_sent:$userId:$userName:$userRole";
        } catch (Exception $e) {
            echo "otp_email_failed:" . $mail->ErrorInfo;
        }

        $stmtOtp->close();
        $stmt->close();
        $conn->close();
        exit();
    }
}

// Continue with new registration
$hashedPassword = password_hash($password, PASSWORD_DEFAULT);

$signupQuery = "INSERT INTO signup (role, name, gender, age, contact, email, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
$stmtSignup = $conn->prepare($signupQuery);
$stmtSignup->bind_param("sssisss", $role, $name, $gender, $age, $contact, $email, $hashedPassword);
$stmtSignup->execute();

$userQuery = "INSERT INTO users (role, name, email, password, date, verified) VALUES (?, ?, ?, ?, NOW(), 0)";
$stmtUser = $conn->prepare($userQuery);
$stmtUser->bind_param("ssss", $role, $name, $email, $hashedPassword);
$stmtUser->execute();
$userId = $stmtUser->insert_id;

$otp = rand(100000, 999999);
$expiry = date("Y-m-d H:i:s", time() + 300);

$stmtOtp = $conn->prepare("
    INSERT INTO otp_codes (email, otp, expiry_time)
    VALUES (?, ?, ?)
    ON DUPLICATE KEY UPDATE otp = VALUES(otp), expiry_time = VALUES(expiry_time)
");
$stmtOtp->bind_param("sss", $email, $otp, $expiry);
$stmtOtp->execute();

$mail = new PHPMailer(true);
try {
    $mail->isSMTP();
    $mail->Host       = 'smtp.gmail.com';
    $mail->SMTPAuth   = true;
    $mail->Username   = 'campuscareapp@gmail.com';
    $mail->Password   = 'qcmx nybd rmuy jnby';
    $mail->SMTPSecure = 'tls';
    $mail->Port       = 587;

    $mail->setFrom('campuscareapp@gmail.com', 'CampusCare');
    $mail->addAddress($email);
    $mail->isHTML(true);
    $mail->Subject = 'Your CampusCare OTP';
    $mail->Body    = "<p>Your OTP is: <strong>$otp</strong></p><p>It will expire in 5 minutes.</p>";

    $mail->send();
    echo "otp_sent:$userId:$name:$role";
} catch (Exception $e) {
    echo "otp_email_failed:" . $mail->ErrorInfo;
}

// Close statements
$stmt->close();
$stmtSignup->close();
$stmtUser->close();
$stmtOtp->close();
$conn->close();
?>
