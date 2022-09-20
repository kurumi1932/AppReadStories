<?php

$conn = mysqli_connect("localhost", "root", "", "appreadstories");

switch ($_SERVER['REQUEST_METHOD']) {
  //-------------get-------------//
  case 'GET':

    $id = $_GET["mataikhoan"];
    //Kiem tra taikhoan co ton tai khong
    $sql_select = "SELECT * FROM taikhoan WHERE mataikhoan = '$id'";
    $response = mysqli_query($conn, $sql_select);

    if (mysqli_num_rows($response) === 1) {
      $row = mysqli_fetch_assoc($response);
  
      $result['mataikhoan'] = $row['mataikhoan'];
      $result['taikhoan'] = $row['taikhoan'];
      $result['matkhau'] = $row['matkhau'];
      $result['email'] = $row['email'];
      $result['tenhienthi'] = $row['tenhienthi'];
      $result['success'] = "1";
      
      echo json_encode($result);
      mysqli_close($conn);
    }else{ 
      $result['success'] = "2";
  
      echo json_encode($result);
      mysqli_close($conn);
    }

    break;
    
  //-------------post-------------//
  case 'POST':

    $id = $_POST["mataikhoan"];
    $password = $_POST['matkhau'];
    $email = $_POST['email'];
    $name = $_POST['tenhienthi'];

    //Kiem tra taikhoan co ton tai khong
    $sql_select = "SELECT * FROM taikhoan WHERE mataikhoan = '$id'";
    $response1 = mysqli_query($conn, $sql_select);

    if (mysqli_num_rows($response1) === 1) {
      $sql_update = "UPDATE taikhoan SET matkhau = '$password', email = '$email', tenhienthi = '$name' WHERE mataikhoan='$id'";
      $response2 = mysqli_query($conn, $sql_update);
      
      if($response2){
        $result['success'] = "1";
        
        echo json_encode($result);
        mysqli_close($conn);
      }else{
        $result['success'] = "2";

        echo json_encode($result);
        mysqli_close($conn);
      }
    }else{
      $result['success'] = "3";
  
      echo json_encode($result);
      mysqli_close($conn);
    }

    break;
  //-------------default-------------//
  default:
  
    $result['success'] = "0";

    echo json_encode($result);
    mysqli_close($conn);
    
    break;
}

?>