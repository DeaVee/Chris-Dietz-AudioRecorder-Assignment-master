package com.xappmedia.audiorecorderharness;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 *
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    public interface FileAdapterListener {
        void onFileSelected(File file);
        void onFileDelete(File file);
    }

    private final ArrayList<File> files;

    private FileAdapterListener listener;

    public FileAdapter() {
        files = new ArrayList<>();
    }

    public void setListener(FileAdapterListener listener) {
        this.listener = listener;
    }

    public void addFile(File file) {
        if (file != null) {
            files.add(file);
            notifyItemInserted(files.size());
        }
    }

    public void removeFile(File file) {
        if (file != null && files.contains(file)) {
            int index = files.indexOf(file);
            files.remove(file);
            notifyItemRemoved(index);
        }
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        return new FileViewHolder(root);
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        holder.bind(files.get(position));
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private final TextView name;

        private File item;

        public FileViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txtName);
            itemView.findViewById(R.id.btnPlay).setOnClickListener(this);
            itemView.findViewById(R.id.btnDelete).setOnClickListener(this);
        }

        public void bind(File item) {
            this.item = item;
            name.setText(item.getName());
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                switch (v.getId()) {
                    case R.id.btnPlay:
                        listener.onFileSelected(item);
                        break;
                    case R.id.btnDelete:
                        listener.onFileDelete(item);
                        break;
                }
            }
        }
    }
}
