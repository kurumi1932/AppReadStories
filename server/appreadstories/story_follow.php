<?php

$conn = mysqli_connect("localhost", "root", "", "appreadstories");

switch ($_SERVER['REQUEST_METHOD']) {
        //-------------get-------------//
    case 'GET':
        $id_account = $_GET['mataikhoan'];

        if (!empty($id_account) && empty($_GET['matruyen'])) {
            $sql_select = "SELECT * FROM truyentheodoi WHERE mataikhoan='$id_account' ORDER BY thoigiancapnhat DESC";
            $response = mysqli_query($conn, $sql_select);
            $result = array();

            if (mysqli_num_rows($response) > 0) {
                while ($row = mysqli_fetch_array($response)) {
                    $index['matruyen'] = $row['matruyen'];
                    $index['tentruyen'] = $row['tentruyen'];
                    $index['tacgia'] = $row['tacgia'];
                    $index['gioihantuoi'] = $row['gioihantuoi'];
                    $index['tongchuong'] = $row['tongchuong'];
                    $index['trangthai'] = $row['trangthai'];
                    $index['anh'] = $row['anh'];

                    array_push($result, $index);
                }
                echo json_encode($result);
                mysqli_close($conn);
            } else {

                $index['matruyen'] = 0;
                $index['tentruyen'] = 0;
                $index['tacgia'] = 0;
                $index['gioihantuoi'] = 0;
                $index['tongchuong'] = 0;
                $index['trangthai'] = 0;
                $index['anh'] = 0;

                array_push($result, $index);

                echo json_encode($result);
                mysqli_close($conn);
            }
        }

        if (!empty($id_account) && !empty($_GET['matruyen'])) {
            $id_story = $_GET['matruyen'];

            $sql_select = "SELECT * FROM truyentheodoi WHERE mataikhoan='$id_account' AND matruyen = '$id_story'";
            $response = mysqli_query($conn, $sql_select);

            if (mysqli_num_rows($response) === 1) {
                $result['success'] = 1;

                echo json_encode($result);
                mysqli_close($conn);
            } else {
                $result['success'] = 2;

                echo json_encode($result);
                mysqli_close($conn);
            }
        }
        break;
        //-------------post-------------//
    case 'POST':

        $id_account = $_POST['mataikhoan'];
        $id_story = $_POST['matruyen'];
        $story = $_POST['tentruyen'];
        $author = $_POST['tacgia'];
        $sum_chapter = $_POST['tongchuong'];
        $status = $_POST['trangthai'];
        $age = $_POST['gioihantuoi'];
        $image = $_POST['anh'];
        $update_time = $_POST['thoigiancapnhat'];

        $sql_select = "SELECT * FROM truyentheodoi WHERE mataikhoan='$id_account' AND matruyen = '$id_story'";
        $response = mysqli_query($conn, $sql_select);

        if (mysqli_num_rows($response) === 1) {
            $sql_delete = "DELETE FROM truyentheodoi WHERE mataikhoan='$id_account' AND matruyen = '$id_story'";
            $response1 = mysqli_query($conn, $sql_delete);

            if ($response1) {
                $result['success'] = 2;

                echo json_encode($result);
                mysqli_close($conn);
            } else {
                $result['success'] = 1;

                echo json_encode($result);
                mysqli_close($conn);
            }
        } else {
            $sql_insert = "INSERT INTO truyentheodoi(mataikhoan, matruyen, tentruyen, tacgia, tongchuong, trangthai, gioihantuoi, anh, thoigiancapnhat) VALUES ('$id_account','$id_story','$story','$author','$sum_chapter','$status','$age','$image','$update_time')";
            $response1 = mysqli_query($conn, $sql_insert);

            if ($response1) {
                $result['success'] = 1;

                echo json_encode($result);
                mysqli_close($conn);
            } else {
                $result['success'] = 2;

                echo json_encode($result);
                mysqli_close($conn);
            }
        }
        break;
        //-------------default-------------//
    default:

        $result['success'] = "0";

        echo json_encode($result);
        mysqli_close($conn);

        break;
}