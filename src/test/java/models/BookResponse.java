package models;

import java.util.List;

public class BookResponse {
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public static class Book {
        private String isbn;
        private String title;
        private String subTitle;
        private String author;
        private String publish_date;
        private String publisher;
        private int pages;
        private String description;
        private String website;

        // Getters and Setters
        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle (String title){
            this.title = title;
        }

        public String getSubTitle () {
            return subTitle;
        }

        public void setSubTitle (String subTitle){
            this.subTitle = subTitle;
        }

        public String getAuthor () {
            return author;
        }

        public void setAuthor (String author){
            this.author = author;
        }

        public String getPublish_date() {
            return publish_date;
        }

        public void setPublish_date (String publish_date){
            this.publish_date = publish_date;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher){
            this.publisher = publisher;
        }

       public int getPages() {
           return pages;
       }

       public void setPages ( int pages){
           this.pages = pages;
       }

       public String getDescription () {
           return description;
       }

       public void setDescription (String description){
           this.description = description;
       }

       public String getWebsite() {
           return website;
       }

       public void setWebsite(String website){
           this.website = website;
       }
    }
}