package tr.edu.bilecik.studentattendancecontrolsystem.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.dift.ui.SwipeToAction;
import tr.edu.bilecik.studentattendancecontrolsystem.Model.User;
import tr.edu.bilecik.studentattendancecontrolsystem.R;

/**
 * Created by gurkanmustafa on 07/10/2015.
 */
public class ManualListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> items;

    public ManualListAdapter(List<User> items) {
        this.items = items;
    }

    /** References to the views for each data item **/
    public class ManualViewHolder extends SwipeToAction.ViewHolder<User> {
        public TextView titleView;

        public ManualViewHolder(View v) {
            super(v);

            titleView = (TextView) v.findViewById(R.id.title);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manuel_attendance_item, parent, false);

        return new ManualViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User item = items.get(position);
        ManualViewHolder vh = (ManualViewHolder) holder;
        vh.titleView.setText(item.getName()+" "+item.getSurname());
        vh.data = item;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
