package com.example.android.android_developer_project_8_news_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * An {@link NewsAdapter} knows how to create a list item layout for each news
 * in the data source (a list of {@link News} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news    is the list of news, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    // Returns a list item view that displays information about the news in the list of news.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the news at the given position in the list of news
        News currentNews = getItem(position);

        // Find the TextView with view ID title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        // get the title
        String title = currentNews.getTitle();
        // Display the current news title in that TextView
        titleView.setText(title);

        // Find the TextView with view ID section_name
        TextView sectionNameView = (TextView) listItemView.findViewById(R.id.section_name);
        // get the section name
        String sectionName = currentNews.getSection();
        // Display the location of the current news in that TextView
        sectionNameView.setText(sectionName);


        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String date = currentNews.getDatePublished();
        // Display the date of the current news in that TextView
        dateView.setText(dateFormation(date));

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

    private String dateFormation (String date) {
        // date formation
        String time = date.substring(11, 16);
        date = date.substring(0, 10);
        date = "Date: " + date + " Time: " + time;
        return date;
    }

}