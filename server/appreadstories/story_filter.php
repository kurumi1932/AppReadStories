<?php

$conn = mysqli_connect("localhost", "root", "", "appreadstories");

if ($_SERVER['REQUEST_METHOD'] == 'GET') {

    $species = $_GET['theloai'];
    $status = $_GET['trangthai'];
    $age = $_GET['gioihantuoi'];

    $species_all = strcmp($species, 'Tất cả');
    $status_all = strcmp($status, 'Tất cả');

    if ($species_all == 0 & $status_all == 0) {
        $sql_select = "SELECT * FROM truyen WHERE gioihantuoi <= '$age' ORDER BY thoigiancapnhat DESC";
    } elseif ($species_all == 0 & $status_all != 0) {
        $sql_select = "SELECT * FROM truyen WHERE gioihantuoi <= '$age' AND trangthai='$status' ORDER BY thoigiancapnhat DESC";
    } elseif ($species_all != 0 & $status_all == 0) {
        $sql_select = "SELECT * FROM truyen WHERE gioihantuoi <= '$age' AND theloai='$species' ORDER BY thoigiancapnhat DESC";
    } else {
        $sql_select = "SELECT * FROM truyen WHERE gioihantuoi <= '$age' AND theloai='$species' AND trangthai='$status' ORDER BY thoigiancapnhat DESC";
    }

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