<?php

$conn = mysqli_connect("localhost", "root", "", "appreadstories");

switch ($_SERVER['REQUEST_METHOD']) {
    //-------------get-------------//
    case 'GET':
        //get list comment
        if(isset($_GET["matruyen"])){
            $id_story = $_GET['matruyen'];

            //Kiem tra taikhoan co ton tai khong
            $sql_select = "SELECT * FROM binhluan WHERE matruyen = '$id_story'";
            $response = mysqli_query($conn, $sql_select);
            $result = array();

            while($row = mysqli_fetch_array($response)) {
                $index['mabinhluan'] = $row['mabinhluan'];
                $index['matruyen'] = $row['matruyen'];
                $index['mataikhoan'] = $row['mataikhoan'];
                $index['tenhienthi'] = $row['tenhienthi'];
                $index['binhluan'] = $row['binhluan'];

                array_push($result, $index);
            }
            echo json_encode($result);
            mysqli_close($conn);
        }
        //delete comment
        if(isset($_GET['mabinhluan'])&& empty($_GET['mataikhoan'])){
            $id_comment = $_GET['mabinhluan'];
            $sql_delete = "DELETE FROM binhluan WHERE mabinhluan = '$id_comment'";
            $response = mysqli_query($conn, $sql_delete);

            if(mysqli_affected_rows($conn) === 1) {// mysql_affected_rows () số hàng bị ảnh hưởng
                $result['success'] = 1;

                echo json_encode($result);
                mysqli_close($conn);
            }else{
                $result['success'] = 2;

                echo json_encode($result);
                mysqli_close($conn);
            }
        }
        //check comment of account
        if(isset($_GET['mabinhluan'])&& isset($_GET['mataikhoan'])){
            $id_comment = $_GET['mabinhluan'];
            $id_account = $_GET['mataikhoan'];
            $sql_select = "SELECT * FROM binhluan WHERE mabinhluan = '$id_comment' AND mataikhoan = '$id_account'";
            $response = mysqli_query($conn, $sql_select);

            if(mysqli_num_rows($response) === 1) {
                $result['success'] = 1;

                echo json_encode($result);
                mysqli_close($conn);
            }else{
                $result['success'] = 2;

                echo json_encode($result);
                mysqli_close($conn);
            }
        }

        break;
    //-------------post-------------//
    case 'POST':
        $id_story = $_POST['matruyen'];
        $id_account = $_POST['mataikhoan'];
        $name = $_POST['tenhienthi'];
        $comment = $_POST['binhluan'];

        $sql_insert = "INSERT INTO binhluan(matruyen, mataikhoan, tenhienthi, binhluan) VALUES ('$id_story','$id_account','$name','$comment')";
        $response = mysqli_query($conn, $sql_insert);

        if ($response){
            $result['success'] = 1;

            echo json_encode($result);
            mysqli_close($conn);
        } else {
            $result['success'] = 2;

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