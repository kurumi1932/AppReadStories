<?php

$conn = mysqli_connect("localhost", "root", "", "appreadstories");

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
  $id = $_POST["mataikhoan"];
  $password = $_POST['matkhau'];
  $email = $_POST['email'];
  $name = $_POST['tenhienthi'];
  $birthday = $_POST["ngaysinh"];

  //Kiem tra taikhoan co ton tai khong
  $sql_select = "SELECT * FROM taikhoan WHERE mataikhoan = '$id'";
  $response1 = mysqli_query($conn, $sql_select);

  if (mysqli_num_rows($response1) === 1) {
    $sql_update = "UPDATE taikhoan SET matkhau = '$password', email = '$email', tenhienthi = '$name', ngaysinh = '$birthday' WHERE mataikhoan='$id'";
    $response2 = mysqli_query($conn, $sql_update);

    if ($response2) {
      $result['accountsuccess'] = "1";

      echo json_encode($result);
      mysqli_close($conn);
    } else {
      $result['accountsuccess'] = "2";

      echo json_encode($result);
      mysqli_close($conn);
    }
  } else {
    $result['accountsuccess'] = "3";

    echo json_encode($result);
    mysqli_close($conn);
  }
}