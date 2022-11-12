<?php

$conn = mysqli_connect("localhost", "root", "", "appreadstories");

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  $username = $_POST["taikhoan"];
  $password = $_POST["matkhau"];
  $email = $_POST["email"];
  $name = $_POST["tenhienthi"];
  $birthday = $_POST["ngaysinh"];

  //Kiem tra taikhoan da ton tai chua
  $sql_select = "SELECT * FROM taikhoan WHERE taikhoan = '$username'";
  $response1 = mysqli_query($conn, $sql_select);

  if (mysqli_num_rows($response1) === 1) {

    $result['accountsuccess'] = "0";

    echo json_encode($result);
    mysqli_close($conn);

  }else {

    $sql_insert = "INSERT INTO taikhoan(taikhoan, matkhau, email, tenhienthi, ngaysinh) VALUES ('$username','$password','$email','$name','$birthday')";
    $response2 = mysqli_query($conn, $sql_insert);

    if ($response2) {

      $result['accountsuccess'] = "1";

      echo json_encode($result);
      mysqli_close($conn);

    }
    else {

      $result['accountsuccess'] = "2";

      echo json_encode($result);
      mysqli_close($conn);

    }
  }
}

?>