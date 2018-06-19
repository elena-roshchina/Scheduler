package example.starfox.sheduler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>  {

    private List<MsgList> messages;

    public MessagesAdapter(List<MsgList> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.messages_item, parent, false);
        return new MessagesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.ViewHolder holder, int position) {
        MsgList itemMessage = messages.get(position);
        holder.itemMsgDate.setText(itemMessage.getStamp());
        holder.itemMsgBody.setText(itemMessage.getMsg());
    }

    @Override
    public int getItemCount() {
        if (messages == null) {
            return 0;
        }
        return messages.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemMsgDate;
        TextView itemMsgBody;

        public ViewHolder(View itemView) {
            super(itemView);
            itemMsgDate = (TextView) itemView.findViewById(R.id.msg_date_item);
            itemMsgBody = (TextView) itemView.findViewById(R.id.msg_body_item);
        }
    }

}
