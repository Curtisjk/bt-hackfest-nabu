package com.bt.frontier;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.bt.R;

/**
 * Created by curtiskennington on 12/11/14.
 */
public class AddNodeDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_node_dialog, null);
        builder.setView(view);

        builder.setMessage(R.string.add_node_dialog_title)
                .setPositiveButton(R.string.add_node_dialog_add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Get location
                        FrontierApp app = (FrontierApp)(getActivity().getApplicationContext());
                        Location currentLocation = app.getCurrentLocation();

                        //Get inputted name
                        String nodeName = ((EditText)view.findViewById(R.id.nodeNameTextField)).getText().toString();

                        //Build up the node
                        Node node = new Node();
                        node.setName(nodeName);
                        node.setLat(currentLocation.getLatitude());
                        node.setLon(currentLocation.getLongitude());
                        node.setOwner(app.getUserId());

                        //Send the data off asynchronously
                        new NodeAddTask().execute(new Object[] {node, getActivity()});
                    }
                })
                .setNegativeButton(R.string.add_node_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}