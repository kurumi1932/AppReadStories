<?php

$conn = mysqli_connect("localhost", "root", "", "appreadstories");

if ($_SERVER['REQUEST_METHOD'] == 'POST') {

  $username = $_POST['taikhoan'];
  $password = $_POST['matkhau'];

  $sql_select = "SELECT * FROM taikhoan WHERE taikhoan='$username' AND matkhau='$password'";
  $response = mysqli_query($conn, $sql_select);

  if (mysqli_num_rows($response) === 1) {

    $row = mysqli_fetch_assoc($response);

    $result['mataikhoan'] = $row['mataikhoan'];
    $result['matkhau'] = $row['matkhau'];
    $result['tenhienthi'] = $row['tenhienthi'];
    $result['email'] = $row['email'];
    $result['ngaysinh'] = $row['ngaysinh'];
    $result['accountsuccess'] = "1";

    echo json_encode($result);
    mysqli_close($conn);
  } else {

    $result['accountsuccess'] = "0";

    echo json_encode($result);
    mysqli_close($conn);
  }
}