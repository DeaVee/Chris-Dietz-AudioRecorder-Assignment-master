package com.xappmedia.audiorecorderharness;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    public interface FileAdapterListener {
        void onFileSelected(File file);
        void onFileDelete(File file);
    }

    private final ArrayList<File> mFiles;

    private FileAdapterListener mListener;

    public FileAdapter() {
        mFiles = new ArrayList<>();
    }

    public void setListener(FileAdapterListener listener) {
        mListener = listener;
    }

    public void addFile(File file) {
        if (file != null) {
            mFiles.add(file);
            notifyItemInserted(mFiles.size());
        }
    }

    public void addFiles(File[] files) {
        if (files != null) {
            addFiles(Arrays.asList(files));
        }
    }

    public void addFiles(Collection<File> files) {
        if (files != null) {
            int startIndex = mFiles.size();
            mFiles.addAll(files);
            notifyItemRangeInserted(startIndex, mFiles.size());
        }
    }

    public void removeFile(File file) {
        if (file != null && mFiles.contains(file)) {
            int index = mFiles.indexOf(file);
            mFiles.remove(file);
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
        holder.bind(mFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
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
            if (mListener != null) {
                switch (v.getId()) {
                    case R.id.btnPlay:
                        mListener.onFileSelected(item);
                        break;
                    case R.id.btnDelete:
                        mListener.onFileDelete(item);
                        break;
                }
            }
        }
    }
}
