<?php

$conn = mysqli_connect("localhost", "root", "", "appreadstories");

switch ($_SERVER['REQUEST_METHOD']) {

    //-------------get-------------//
    case 'GET':
        $id_story = $_GET['matruyen'];

        //get list chapter
         if(!empty($id_story) && empty($_GET['machuong']) && empty($_GET['sochuong'])){

            if ($id_story>0) {

                $sql_select = "SELECT * FROM chuongtruyen WHERE matruyen='$id_story'";
                $response = mysqli_query($conn, $sql_select);
                $result = array();
        
                while($row = mysqli_fetch_array($response)) {
        
                    $index['matruyen'] = $row['matruyen'];
                    $index['machuong'] = $row['machuong'];
                    $index['sochuong'] = $row['sochuong'];
                    $index['tenchuong'] = $row['tenchuong'];
                    $index['thoigiandang'] = $row['thoigiandang'];
        
                    array_push($result, $index);
        
                }
        
                echo json_encode($result);
                mysqli_close($conn);
        
            }

        }

        //search chapter
        if(!empty($id_story) && empty($_GET['machuong']) && !empty($_GET['sochuong'])){
            $number_chapter=$_GET['sochuong'];
            $str = '%';
            $str_search = $number_chapter.$str;

            $sql_select = "SELECT * FROM chuongtruyen WHERE matruyen='$id_story' AND sochuong LIKE '$str_search'";
            $response = mysqli_query($conn, $sql_select);
            $result = array();
        
            while($row = mysqli_fetch_array($response)) {
    
                $index['matruyen'] = $row['matruyen'];
                $index['machuong'] = $row['machuong'];
                $index['sochuong'] = $row['sochuong'];
                $index['tenchuong'] = $row['tenchuong'];
                $index['thoigiandang'] = $row['thoigiandang'];
    
                array_push($result, $index);
    
            }
    
            echo json_encode($result);
            mysqli_close($conn);
        }

        //get chapter
        if(!empty($id_story) && !empty($_GET['machuong']) && !empty($_GET['thaydoichuong']) && empty($_GET['sochuong'])){

            $id_chapter = $_GET['machuong'];
            $num = $_GET['thaydoichuong'];

            if ($id_story>0 && $id_chapter>0) {
                //previous_chapter
                if($num == 1){
                    $sql_select = "SELECT machuong FROM chuongtruyen WHERE matruyen='$id_story' ORDER BY machuong ASC LIMIT 1";
                    $response = mysqli_query($conn, $sql_select);

                    if (mysqli_num_rows($response)===1) {
                        $row = mysqli_fetch_assoc($response);

                        $id_chapter_first = $row['machuong'];

                        if ($id_chapter > $id_chapter_first) {
                            $sql_select1 = "SELECT * FROM chuongtruyen WHERE matruyen='$id_story' AND machuong < '$id_chapter' AND machuong >= '$id_chapter_first' ORDER BY machuong DESC LIMIT 1";
                            $response1 = mysqli_query($conn, $sql_select1);
                            
                            if(mysqli_num_rows($response1)===1) {
                                $row = mysqli_fetch_array($response1);

                                $result['matruyen'] = $row['matruyen'];
                                $result['machuong'] = $row['machuong'];
                                $result['sochuong'] = $row['sochuong'];
                                $result['tenchuong'] = $row['tenchuong'];
                                $result['nguoidang'] = $row['nguoidang'];
                                $result['thoigiandang'] = $row['thoigiandang'];
                                $result['noidung'] = $row['noidung'];
                    
                                echo json_encode($result);
                                mysqli_close($conn);
                            }
                        } else{
                            $result['matruyen'] = 0;
                            $result['machuong'] = 0;
                            $result['sochuong'] = 0;
                            $result['tenchuong'] = 0;
                            $result['nguoidang'] = 0;
                            $result['thoigiandang'] = 0;
                            $result['noidung'] = 0;
                
                            echo json_encode($result);
                            mysqli_close($conn);
                        }
                    }
                }
                //chapter
                if($num == 2){
                    $sql_select = "SELECT * FROM chuongtruyen WHERE matruyen='$id_story' AND machuong='$id_chapter'";
                    $response = mysqli_query($conn, $sql_select);
            
                    if(mysqli_num_rows($response)===1) {
            
                        $row = mysqli_fetch_array($response);

                        $result['matruyen'] = $row['matruyen'];
                        $result['machuong'] = $row['machuong'];
                        $result['sochuong'] = $row['sochuong'];
                        $result['tenchuong'] = $row['tenchuong'];
                        $result['nguoidang'] = $row['nguoidang'];
                        $result['thoigiandang'] = $row['thoigiandang'];
                        $result['noidung'] = $row['noidung'];
            
                        echo json_encode($result);
                        mysqli_close($conn);

                    }else{
                        $result['matruyen'] = 0;
                        $result['machuong'] = 0;
                        $result['sochuong'] = 0;
                        $result['tenchuong'] = 0;
                        $result['nguoidang'] = 0;
                        $result['thoigiandang'] = 0;
                        $result['noidung'] = 0;
            
                        echo json_encode($result);
                        mysqli_close($conn);
                    }
                }
                //next_chapter
                if($num == 3) {
                    $sql_select = "SELECT machuong FROM chuongtruyen WHERE matruyen='$id_story' ORDER BY machuong DESC LIMIT 1";
                    $response = mysqli_query($conn, $sql_select);

                    if (mysqli_num_rows($response)===1) {
                        $row = mysqli_fetch_assoc($response);

                        $id_chapter_final = $row['machuong'];
                        
                        if ($id_chapter < $id_chapter_final) {
                            $sql_select1 = "SELECT * FROM chuongtruyen WHERE  matruyen='$id_story' AND machuong>'$id_chapter' AND machuong<='$id_chapter_final' ORDER BY machuong ASC LIMIT 1";
                            $response1 = mysqli_query($conn, $sql_select1);

                            if(mysqli_num_rows($response1)===1) {
                                $row = mysqli_fetch_array($response1);

                                $result['matruyen'] = $row['matruyen'];
                                $result['machuong'] = $row['machuong'];
                                $result['sochuong'] = $row['sochuong'];
                                $result['tenchuong'] = $row['tenchuong'];
                                $result['nguoidang'] = $row['nguoidang'];
                                $result['thoigiandang'] = $row['thoigiandang'];
                                $result['noidung'] = $row['noidung'];
                    
                                echo json_encode($result);
                                mysqli_close($conn);
                            }
                        } else{
                            $result['matruyen'] = 0;
                            $result['machuong'] = 0;
                            $result['sochuong'] = 0;
                            $result['tenchuong'] = 0;
                            $result['nguoidang'] = 0;
                            $result['thoigiandang'] = 0;
                            $result['noidung'] = 0;
                
                            echo json_encode($result);
                            mysqli_close($conn);
                        }
                    }   
                }
            }      
        }
        
        break;
    //-------------post-------------//
    case 'POST':
        $id_story = $_POST['matruyen'];

        $sql_select = "SELECT machuong FROM chuongtruyen WHERE matruyen='$id_story' ORDER BY machuong ASC LIMIT 1";
        $response = mysqli_query($conn, $sql_select);

        if (mysqli_num_rows($response)===1) {
            $row = mysqli_fetch_assoc($response);

            $result['machuong'] = $row['machuong'];

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