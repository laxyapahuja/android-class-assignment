package com.example.assignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    private List<Assessment> assessments;

    public AssessmentAdapter(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public void addAssessment(Assessment newAssessment) {
        assessments.add(0, newAssessment);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessment_item, parent, false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        Assessment assessment = assessments.get(position);
        holder.weightTextView.setText("Weight: " + assessment.getWeight());
        holder.heightTextView.setText("Height: " + assessment.getHeight());
        holder.bpTextView.setText("BP: " + assessment.getBp());
        holder.dateTextView.setText("Date: " + assessment.getDate());
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public static class AssessmentViewHolder extends RecyclerView.ViewHolder {
        public TextView weightTextView, heightTextView, bpTextView, dateTextView;

        public AssessmentViewHolder(View view) {
            super(view);
            weightTextView = view.findViewById(R.id.weightTextView);
            heightTextView = view.findViewById(R.id.heightTextView);
            bpTextView = view.findViewById(R.id.bpTextView);
            dateTextView = view.findViewById(R.id.dateTextView);
        }
    }
}
