package example.starfox.sheduler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.ViewHolder> {

    private List<SubjectsList> marks;

    public MarksAdapter(List<SubjectsList> marks) {
        this.marks = marks;
    }

    @NonNull
    @Override
    public MarksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marks_item, parent, false);
        return new MarksAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MarksAdapter.ViewHolder holder, int position) {
        SubjectsList itemSubjectMark = marks.get(position);

        holder.itemSubject.setText(itemSubjectMark.getSubject());
        holder.itemMark.setText(itemSubjectMark.getMark());
        holder.itemDate.setText(itemSubjectMark.getDay());
    }

    @Override
    public int getItemCount() {
        if (marks == null) {
            return 0;
        }
        return marks.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemSubject;
        TextView itemMark;
        TextView itemDate;

        public ViewHolder(View itemView) {
            super(itemView);
            itemSubject = (TextView) itemView.findViewById(R.id.subj_marks_item);
            itemMark = (TextView) itemView.findViewById(R.id.mark_item);
            itemDate = (TextView) itemView.findViewById(R.id.date_exam_item);

        }
    }
}
