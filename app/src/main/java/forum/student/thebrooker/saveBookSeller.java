package forum.student.thebrooker;

public class saveBookSeller {
    public String bookTitle, bookAuthor, bookRelease, bookGenre, type, postDate, price, descriptions, uid, image;

    public saveBookSeller(String bookTitle, String bookAuthor, String bookRelease, String bookGenre, String type, String postDate, String price, String descriptions, String uid, String image) {
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookRelease = bookRelease;
        this.bookGenre = bookGenre;
        this.type = type;
        this.postDate = postDate;
        this.price = price;
        this.descriptions = descriptions;
        this.uid = uid;
        this.image = image;
    }

    public saveBookSeller() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookRelease() {
        return bookRelease;
    }

    public void setBookRelease(String bookRelease) {
        this.bookRelease = bookRelease;
    }

    public String getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(String bookGenre) {
        this.bookGenre = bookGenre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostDate(){
        return postDate;
    }

    public void setPostDate(String postDate){
        this.postDate = postDate;
    }
}
