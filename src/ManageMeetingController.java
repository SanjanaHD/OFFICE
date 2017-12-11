import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageMeetingController {

    public void addMeeting(ActionEvent e) throws IOException{
        Stage addMeetingStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AddMeeting.fxml"));
        Scene scene = new Scene(root);
        addMeetingStage.setTitle("Add New Meeting");
        addMeetingStage.setScene(scene);
        addMeetingStage.setResizable(false);
        addMeetingStage.show();
    }

    public void editMeeting(ActionEvent e) throws IOException{
        Stage searchMeetingStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("EditMeeting.fxml"));
        Scene scene = new Scene(root);
        searchMeetingStage.setTitle("Search & Edit Meeting");
        searchMeetingStage.setScene(scene);
        searchMeetingStage.setResizable(false);
        searchMeetingStage.show();
    }

    public void deleteMeeting(ActionEvent e) throws IOException{
        Stage deleteMeetingStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("DeleteMeeting.fxml"));
        Scene scene = new Scene(root);
        deleteMeetingStage.setTitle("Search & Delete Meeting");
        deleteMeetingStage.setScene(scene);
        deleteMeetingStage.setResizable(false);
        deleteMeetingStage.show();
    }
}
