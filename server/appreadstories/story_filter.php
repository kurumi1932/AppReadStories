<?php

$conn = mysqli_connect("localhost", "root", "", "appreadstories");

if ($_SERVER['REQUEST_METHOD'] == 'GET') {
  
    $species = $_GET['theloai'];
    $status = $_GET['trangthai'];

    $species_all = strcmp($species,'Tất cả');
    $status_all = strcmp($status,'Tất cả');

    if($species_all == 0 & $status_all == 0){
        $sql_select = "SELECT * FROM truyen GROUP BY matruyen DESC"; 
    }elseif ($species_all == 0 & $status_all != 0) {
        $sql_select = "SELECT * FROM truyen WHERE trangthai='$status' GROUP BY matruyen DESC"; 
    }elseif ($species_all != 0 & $status_all == 0) {
        $sql_select = "SELECT * FROM truyen WHERE theloai='$species' GROUP BY matruyen DESC";
    }else {
        $sql_select = "SELECT * FROM truyen WHERE theloai='$species' AND trangthai='$status' GROUP BY matruyen DESC";
    }

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

?>