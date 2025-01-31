<?php

$conn = mysqli_connect("localhost", "root", "", "appreadstories");

switch ($_SERVER['REQUEST_METHOD']) {
        //-------------get-------------//
    case 'GET':
        if (isset($_GET['matruyen'])) {
            $id_story = $_GET['matruyen'];

            //get list rate
            if (empty($_GET["mataikhoan"])) {

                $sql_select = "SELECT * FROM danhgia WHERE matruyen = '$id_story'";
                $response = mysqli_query($conn, $sql_select);
                $result = array();

                while ($row = mysqli_fetch_array($response)) {
                    $index['madanhgia'] = $row['madanhgia'];
                    $index['matruyen'] = $row['matruyen'];
                    $index['mataikhoan'] = $row['mataikhoan'];
                    $index['tenhienthi'] = $row['tenhienthi'];
                    $index['diemdanhgia'] = $row['diemdanhgia'];
                    $index['danhgia'] = $row['danhgia'];

                    array_push($result, $index);
                }
                echo json_encode($result);
                mysqli_close($conn);
            }
            //check rate of account
            if (isset($_GET['mataikhoan'])) {
                $id_account = $_GET['mataikhoan'];
                $sql_select = "SELECT * FROM danhgia WHERE matruyen = '$id_story' AND mataikhoan = '$id_account'";
                $response = mysqli_query($conn, $sql_select);

                if (mysqli_num_rows($response) === 1) {
                    $row = mysqli_fetch_array($response);

                    $result['madanhgia'] = $row['madanhgia'];
                    $result['matruyen'] = $row['matruyen'];
                    $result['mataikhoan'] = $row['mataikhoan'];
                    $result['tenhienthi'] = $row['tenhienthi'];
                    $result['diemdanhgia'] = $row['diemdanhgia'];
                    $result['danhgia'] = $row['danhgia'];
                    $result['ratesuccess'] = 1;

                    echo json_encode($result);
                    mysqli_close($conn);
                } else {
                    $result['ratesuccess'] = 2;

                    echo json_encode($result);
                    mysqli_close($conn);
                }
            }
        } else {
            //delete rate
            if (isset($_GET['madanhgia'])) {
                $id_rate = $_GET['madanhgia'];
                $sql_delete = "DELETE FROM danhgia WHERE madanhgia = '$id_rate'";
                $response = mysqli_query($conn, $sql_delete);

                if (mysqli_affected_rows($conn) === 1) { // mysql_affected_rows () số hàng bị ảnh hưởng
                    $result['ratesuccess'] = 1;

                    echo json_encode($result);
                    mysqli_close($conn);
                } else {
                    $result['ratesuccess'] = 2;

                    echo json_encode($result);
                    mysqli_close($conn);
                }
            }
        }

        break;
        //-------------post-------------//
    case 'POST':
        //add rate
        $rate_point = $_POST['diemdanhgia'];
        $rate = $_POST['danhgia'];

        if (empty($_POST['madanhgia'])) {
            //add rate
            $id_story = $_POST['matruyen'];
            $id_account = $_POST['mataikhoan'];
            $name = $_POST['tenhienthi'];

            $sql_insert = "INSERT INTO danhgia(matruyen, mataikhoan, tenhienthi,diemdanhgia, danhgia) VALUES ('$id_story','$id_account','$name','$rate_point','$rate')";
            $response = mysqli_query($conn, $sql_insert);
        } else {
            //update rate
            $id_rate = $_POST['madanhgia'];

            $sql_update = "UPDATE danhgia SET diemdanhgia = '$rate_point', danhgia = '$rate' WHERE madanhgia='$id_rate'";
            $response = mysqli_query($conn, $sql_update);
        }

        if (mysqli_affected_rows($conn) === 1) {
            $result['ratesuccess'] = 1;

            echo json_encode($result);
            mysqli_close($conn);
        } else {
            $result['ratesuccess'] = 2;

            echo json_encode($result);
            mysqli_close($conn);
        }

        break;
        //-------------default-------------//
    default:

        $result['ratesuccess'] = "0";

        echo json_encode($result);
        mysqli_close($conn);

        break;
}