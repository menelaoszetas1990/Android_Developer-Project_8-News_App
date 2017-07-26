package com.example.android.android_developer_project_8_news_app;

// An {@link News} object contains information related to a single earthquake.
public class News {

    // title of the news
    private String mTitle;

    // section name
    private String mSectionName;

    // date published
    private String mDatePublished;

    // Website URL of the news
    private String mUrl;

    /**
     * Constructs a new {@link News} object.
     *
     * @param title         is the title of the news
     * @param section       is the section of the news
     * @param datePublished is the publication date of the news
     * @param url           is the website URL to find more details about the news
     */
    public News(String title, String section, String datePublished, String url) {
        mTitle = title;
        mSectionName = section;
        mDatePublished = datePublished;
        mUrl = url;
    }

    // Returns the title of the news.
    public String getTitle() {
        return mTitle;
    }

    // Returns the section of the news.
    public String getSection() {
        return mSectionName;
    }

    // Returns the publication date of the news
    public String getDatePublished() {
        return mDatePublished;
    }

    // Returns the website URL to find more information about the news.
    public String getUrl() {
        return mUrl;
    }
}