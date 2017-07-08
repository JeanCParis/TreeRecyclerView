package todo.jcp.mobile.treerecyclerview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.activity_main);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new NodeAdapter(new Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        }, 0, new String[]{"1", "2", "3"}));
    }

    private class NodeAdapter extends Adapter {
        Adapter parentAdapter;
        NodeAdapter childAdapter;
        String[] data;
        int startPosition;


        public NodeAdapter(Adapter parentAdapter, int startPosition, String[] data) {
            this.data = data;
            this.parentAdapter = parentAdapter;
            this.startPosition = startPosition;
        }

        public int getStartPosition() {
            return startPosition;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return data.length + parentAdapter.getItemCount();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(getLayoutInflater().inflate(R.layout.item_view, parent, false)) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (startPosition <= position && position < startPosition + data.length) {
                ((TextView) holder.itemView).setText("" + data[position - startPosition]);
                ((TextView) holder.itemView).setTextColor(
                        childAdapter != null && childAdapter.getStartPosition() == position + 1 ?
                                Color.RED : Color.WHITE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (childAdapter == null || childAdapter.getStartPosition() != position + 1) {
                            childAdapter = new NodeAdapter(NodeAdapter.this, position + 1,
                                    new String[]{new StringBuilder().append(data[position-startPosition]).append(".").append("1").toString(),
                                            new StringBuilder().append(data[position-startPosition]).append(".").append("2").toString(),
                                            new StringBuilder().append(data[position-startPosition]).append(".").append("3").toString()});
                            recyclerView.setAdapter(childAdapter);
                            layoutManager.scrollToPosition(position+1);
                        } else {
                            childAdapter = null;
                            recyclerView.setAdapter(NodeAdapter.this);
                            layoutManager.scrollToPosition(position);
                        };
                    }
                });
            } else {
                parentAdapter.onBindViewHolder(holder, position < startPosition ? position : position - data.length);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }
    }
}