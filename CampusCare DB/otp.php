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

if (isset($_POST['email']) && isset($_POST['otp'])) {
    $email = $_POST['email'];
    $otp = $_POST['otp'];

    // Get latest OTP and user details
    $stmt = $conn->prepare("
        SELECT users.id, users.name, otp_codes.otp, otp_codes.expiry_time
        FROM otp_codes
        JOIN users ON users.email = otp_codes.email
        WHERE otp_codes.email=?
        ORDER BY otp_codes.expiry_time DESC
        LIMIT 1
    ");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result && $result->num_rows > 0) {
        $row = $result->fetch_assoc();
        $correctOtp = $row['otp'];
        $expiry_time = $row['expiry_time'];

        if ($otp === $correctOtp && strtotime($expiry_time) > time()) {
            $userId = $row['id'];
            $userName = $row['name'];

          
            $update = $conn->prepare("UPDATE users SET verified = 1 WHERE email = ?");
            $update->bind_param("s", $email);
            $update->execute();
            $update->close();

            $delete = $conn->prepare("DELETE FROM otp_codes WHERE email=?");
            $delete->bind_param("s", $email);
            $delete->execute();
            $delete->close();

            echo "otp_verified:$userId:$userName";
        } else {
            echo "invalid_or_expired_otp";
        }
    } else {
        echo "invalid_or_expired_otp";
    }

    $stmt->close();
} else {
    echo "missing_parameters";
}

mysqli_close($conn);
?>