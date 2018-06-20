package example.starfox.sheduler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<ScheduleModel> schedule;

    public ScheduleAdapter(List<ScheduleModel> schedule) {
        this.schedule = schedule;
    }

    @NonNull
    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
        ScheduleModel itemSchedule = schedule.get(position);

        holder.itemData.setText(itemSchedule.getDate());
        holder.itemSubj.setText(itemSchedule.getSubj());
        holder.itemStuff.setText(itemSchedule.getStaff());
        String audience = "ауд. ";
        audience += itemSchedule.getRoom();
        holder.itemRoom.setText(audience);
    }

    @Override
    public int getItemCount() {
        if (schedule == null) {
            return 0;
        }
        return schedule.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemData;
        TextView itemSubj;
        TextView itemStuff;
        TextView itemRoom;

        private ViewHolder(View itemView) {
            super(itemView);
            itemData = (TextView) itemView.findViewById(R.id.data_item);
            itemSubj = (TextView) itemView.findViewById(R.id.subj_item);
            itemStuff = (TextView) itemView.findViewById(R.id.staff_item);
            itemRoom = (TextView) itemView.findViewById(R.id.room_item);
        }
    }
}
