package azdeveloper.com.helpme;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mohammad Shahzeb on 8/8/2018.
 */

public class ListAdaptor extends ListOfPlaces {
    public ListAdaptor(Context context, Cursor c) {
        super(context, c, 0 );
    }

        public View getView(int position , View convertView , ViewGroup parent){
        View ListItemView = convertView;

        if (ListItemView==null){
            ListItemView = LayoutInflater.from(ListItemView.getContext()).inflate(R.layout.list_item,parent,false);
        }


        TextView nameTextView = ListItemView.findViewById(R.id.place_name);
        TextView detailTextView = ListItemView.findViewById(R.id.place_detail);

      //  nameTextView.setText();
      //  detailTextView.setText();

        return ListItemView;

    }
}
