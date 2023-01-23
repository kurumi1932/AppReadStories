<?php

$conn = mysqli_connect("localhost", "root", "", "appreadstories");

switch ($_SERVER['REQUEST_METHOD']) {

        //-------------get-------------//
    case 'GET':
        if (isset($_GET['matruyen']) && empty($_GET['gioihantuoi'])) {
            $id_story = $_GET['matruyen'];
            //check like story
            if (isset($_GET['mataikhoan'])) {
                $id_account = $_GET['mataikhoan'];

                $sql_select = "SELECT * FROM tuongtac WHERE matruyen = '$id_story' AND mataikhoan = '$id_account'";
                $response = mysqli_query($conn, $sql_select);

                if (mysqli_num_rows($response) === 1) {
                    $row = mysqli_fetch_assoc($response);

                    if ($row['thich'] == 0) {
                        $result['storysuccess'] = 0;
                    } else {
                        $result['storysuccess'] = 1;
                    }
                    $result['tylechuongdadoc'] = $row['tylechuongdadoc'];
                } else {
                    $sql_insert = "INSERT INTO tuongtac(matruyen, mataikhoan) VALUES ('$id_story','$id_account')";
                    $response1 = mysqli_query($conn, $sql_insert);

                    $result['storysuccess'] = 0;
                    $result['tylechuongdadoc'] = 0;
                }

                $sql_select1 = "SELECT * FROM truyen WHERE matruyen = '$id_story'";
                $response1 = mysqli_query($conn, $sql_select1);

                if (mysqli_num_rows($response1) === 1) {
                    $row = mysqli_fetch_assoc($response1);

                    $result['luotthich'] = $row['luotthich'];
                }

                echo json_encode($result);
                mysqli_close($conn);
            } else {
                // get story
                $sql_select = "SELECT * FROM truyen WHERE matruyen = '$id_story'";
                $response = mysqli_query($conn, $sql_select);

                if (mysqli_num_rows($response) === 1) {
                    $row = mysqli_fetch_assoc($response);

                    $result['matruyen'] = $row['matruyen'];
                    $result['tentruyen'] = $row['tentruyen'];
                    $result['tacgia'] = $row['tacgia'];
                    $result['gioihantuoi'] = $row['gioihantuoi'];
                    $result['theloai'] = $row['theloai'];
                    $result['tongchuong'] = $row['tongchuong'];
                    $result['trangthai'] = $row['trangthai'];
                    $result['gioithieu'] = $row['gioithieu'];
                    $result['thoigiancapnhat'] = $row['thoigiancapnhat'];
                    $result['anh'] = $row['anh'];
                    $result['luotxem'] = $row['luotxem'];
                    $result['luotthich'] = $row['luotthich'];
                    $result['luotbinhluan'] = $row['luotbinhluan'];
                    $result['diemdanhgia'] = $row['diemdanhgia'];

                    echo json_encode($result);
                    mysqli_close($conn);
                }
            }
        }

        if (empty($_GET['matruyen']) && isset($_GET['gioihantuoi'])) {
            $age = $_GET['gioihantuoi'];

            //get list story
            if (empty($_GET['tentruyen'])) {

                $sql_select = "SELECT * FROM truyen WHERE gioihantuoi <= '$age' ORDER BY thoigiancapnhat DESC";
                $response = mysqli_query($conn, $sql_select);
                $result = array();

                while ($row = mysqli_fetch_array($response)) {
                    $index['matruyen'] = $row['matruyen'];
                    $index['tentruyen'] = $row['tentruyen'];
                    $index['tacgia'] = $row['tacgia'];
                    $index['gioihantuoi'] = $row['gioihantuoi'];
                    $index['theloai'] = $row['theloai'];
                    $index['tongchuong'] = $row['tongchuong'];
                    $index['trangthai'] = $row['trangthai'];
                    $index['gioithieu'] = $row['gioithieu'];
                    $index['thoigiancapnhat'] = $row['thoigiancapnhat'];
                    $index['anh'] = $row['anh'];

                    array_push($result, $index);
                }
                echo json_encode($result);
                mysqli_close($conn);
            }

            //search story
            if (isset($_GET['tentruyen'])) {
                $story_name = $_GET['tentruyen'];
                $str = '%';
                $str_search = $str . $story_name . $str;

                $sql_select = "SELECT * FROM truyen WHERE tentruyen like '$str_search' AND gioihantuoi <= '$age'";
                $response = mysqli_query($conn, $sql_select);
                $result = array();

                while ($row = mysqli_fetch_array($response)) {
                    $index['matruyen'] = $row['matruyen'];
                    $index['tentruyen'] = $row['tentruyen'];
                    $index['tacgia'] = $row['tacgia'];
                    $index['gioihantuoi'] = $row['gioihantuoi'];
                    $index['theloai'] = $row['theloai'];
                    $index['tongchuong'] = $row['tongchuong'];
                    $index['trangthai'] = $row['trangthai'];
                    $index['gioithieu'] = $row['gioithieu'];
                    $index['thoigiancapnhat'] = $row['thoigiancapnhat'];
                    $index['anh'] = $row['anh'];

                    array_push($result, $index);
                }
                echo json_encode($result);
                mysqli_close($conn);
            }
        }
        break;
        //-------------post-------------//
    case 'POST':
        $id_story = $_POST['matruyen'];
        $id_account = $_POST['mataikhoan'];

        //like story
        $sql_select = "SELECT * FROM tuongtac WHERE matruyen = '$id_story' AND mataikhoan = '$id_account'";
        $response = mysqli_query($conn, $sql_select);

        if (mysqli_num_rows($response) === 1) {
            $row = mysqli_fetch_assoc($response);

            if ($row['thich'] == 0) {
                $sql_update = "UPDATE tuongtac SET thich=1 WHERE matruyen = '$id_story' AND mataikhoan = '$id_account'";
            } else {
                $sql_update = "UPDATE tuongtac SET thich=0 WHERE matruyen = '$id_story' AND mataikhoan = '$id_account'";
            }

            $response1 = mysqli_query($conn, $sql_update);

            if ($response1) {
                $result['storysuccess'] = 1;

                echo json_encode($result);
                mysqli_close($conn);
            }
        }

        break;
        //-------------default-------------//
    default:

        $result['storysuccess'] = "0";

        echo json_encode($result);
        mysqli_close($conn);

        break;
}