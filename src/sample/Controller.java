package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private String data="";
    private FileChooser fileChooser=new FileChooser();
    private File file;
@FXML
private Label label;
@FXML
private Button button;
@FXML
public TextField search;
@FXML
private Button btn;
@FXML
public void chooseFile(MouseEvent event) throws IOException, SQLException {
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT File","*.txt"));
    file=fileChooser.showOpenDialog(null);
    if(file!=null)
    {
        label.setText(file.toString());
        data=new String(Files.readAllBytes(Paths.get(file.toString())));
       // System.out.println(data);
        entrydata();


    }
}

private void entrydata() throws SQLException {
    String[] s=data.split("[ ,.?]");
    List<String> list=new ArrayList<>();
    Connection conn=null;
    PreparedStatement ps=null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionary","root","");
        String sql1="DELETE FROM datahandle";
        ps=conn.prepareStatement(sql1);
        ps.execute();
        String sql="INSERT INTO datahandle(words) VALUES(?)";
        for(int i=0;i<s.length;i++) {
            if(list.contains(s[i])==false) {
                list.add(s[i]);
               // System.out.println(s[i]);
                ps = conn.prepareStatement(sql);
                ps.setString(1,s[i]);
                ps.executeUpdate();
            }
        }
        Alert a=new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("UPLOAD SUCCESSFUL");
        a.setHeaderText("SEARCH NOW");
        a.setContentText("You can search now");
        a.showAndWait();
        btn.setVisible(true);
        search.setVisible(true);
    }
    catch (Exception e){
        Alert a=new Alert(Alert.AlertType.ERROR);
        a.setTitle("ERROR");
        a.setHeaderText("UNABLE TO CONNECT");
        a.setContentText(e.getMessage());
        a.showAndWait();

    }
    finally {
        conn.close();
        ps.close();
    }

}
@FXML
public void searchData(MouseEvent event) throws SQLException {
    Connection conn=null;
    PreparedStatement ps=null;
    ResultSet rs=null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/Dictionary","root","");
        String sql="SELECT* FROM datahandle WHERE words=?";
        ps=conn.prepareStatement(sql);
        ps.setString(1,search.getText());
        rs=ps.executeQuery();
        if(rs.next())
        {
            Alert a=new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("FOUND");
            a.setHeaderText("TRUE");
            a.setContentText(search.getText()+" Found in TXT file");
            a.showAndWait();
        }
        else
        {
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERROR");
            a.setHeaderText("FALSE");
            a.setContentText(search.getText()+" NOT FOUND");
            a.showAndWait();
        }
        search.setText("");
    }
    catch (Exception e){
        Alert a=new Alert(Alert.AlertType.ERROR);
        a.setTitle("SQL ERROR");
        a.setHeaderText("Sorry !! THere is some Technical Issue");
        a.setContentText(e.getMessage());
        a.showAndWait();

    }
    finally {
        conn.close();
        ps.close();
    }
}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    btn.setVisible(false);
    search.setVisible(false);

    }
}
