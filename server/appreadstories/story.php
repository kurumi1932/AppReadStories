<?php

$conn = mysqli_connect("localhost", "root", "", "appreadstories");

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    if (isset($_GET['matruyen'])) {
        $id = $_GET['matruyen'];
        //get list story
        if ($id == 0) {
            $sql_select = "SELECT * FROM truyen GROUP BY matruyen DESC";
            $response = mysqli_query($conn, $sql_select);
            $result = array();

            while($row = mysqli_fetch_array($response)) {
                $index['matruyen'] = $row['matruyen'];
                $index['tentruyen'] = $row['tentruyen'];
                $index['tacgia'] = $row['tacgia'];
                $index['theloai'] = $row['theloai'];
                $index['sochuong'] = $row['sochuong'];
                $index['trangthai'] = $row['trangthai'];
                $index['gioithieu'] = $row['gioithieu'];
                $index['thoigiancapnhat'] = $row['thoigiancapnhat'];
                $index['anh'] = $row['anh'];

                array_push($result, $index);
            }
            echo json_encode($result);
            mysqli_close($conn);
        } 
        // get story
        if ($id > 0) {
            $sql_select = "SELECT * FROM truyen WHERE matruyen = '$id'";
            $response = mysqli_query($conn, $sql_select);

            if(mysqli_num_rows($response) === 1){
                $row = mysqli_fetch_assoc($response);
                
                $result['matruyen'] = $row['matruyen'];
                $result['tentruyen'] = $row['tentruyen'];
                $result['tacgia'] = $row['tacgia'];
                $result['theloai'] = $row['theloai'];
                $result['sochuong'] = $row['sochuong'];
                $result['trangthai'] = $row['trangthai'];
                $result['gioithieu'] = $row['gioithieu'];
                $result['thoigiancapnhat'] = $row['thoigiancapnhat'];
                $result['anh'] = $row['anh'];

                echo json_encode($result);
                mysqli_close($conn);
            }
        }
    }
    //search story
    if(isset($_GET['tentruyen'])){
        $story_name = $_GET['tentruyen'];
        $str = '%';
        $str_search = $str.$story_name.$str;
        
        $sql_select = "SELECT * FROM truyen WHERE tentruyen like '$str_search'";
        $response = mysqli_query($conn, $sql_select);
        $result = array();

        while($row = mysqli_fetch_array($response)) {
            $index['matruyen'] = $row['matruyen'];
            $index['tentruyen'] = $row['tentruyen'];
            $index['tacgia'] = $row['tacgia'];
            $index['theloai'] = $row['theloai'];
            $index['sochuong'] = $row['sochuong'];
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

?>