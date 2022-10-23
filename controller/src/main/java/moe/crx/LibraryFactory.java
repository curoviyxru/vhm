package moe.crx;

public class LibraryFactory {

    private static Library library;

    public static Library getLibrary() {
        if (library != null)
            return library;

        library = new Library();

        library.add("Dav Pilkey", "Mothering Heights", 2021, 1_295_470);
        library.add("Mark R. Levin", "American Marxism", 2021, 1_025_638);
        library.add("Jeff Kinney", "Big Shot", 2021, 985_308);
        library.add("Charlie Macksey", "The Boy, The Mole, The Fox and the Horse", 2019, 954_434);
        library.add("James Clear", "Atomic Habits", 2018, 894_163);
        library.add("Colleen Hoover", "It Ends With Us", 2016, 768_693);
        library.add("Eric Carle", "The Very Hungry Caterpillar", 1994, 731_075);
        library.add("Dr. Seuss", "Oh, the Places You’ll Go!", 1990, 727_341);
        library.add("Don Miguel Ruiz", "The Four Agreements", 1997, 713_696);
        library.add("Kristen Hannah", "The Four Winds", 2021, 711_705);
        library.add("Adam Silvera", "They Both Die at the End", 2018, 684_982);
        library.add("Madeline Miller", "The Song of Achilles", 2012, 652_906);
        library.add("Matt Haig", "The Midnight Library", 2020, 652_763);
        library.add("Delia Owens", "Where the Crawdads Sing", 2021, 625_599);
        library.add("Dav Pilkey", "Cat Kid Comic Club", 2020, 617_821);
        library.add("Hepworth/Warnes", "I Love You to the Moon and Back", 2015, 608_498);
        library.add("Bessel van der Kolk", "The Body Keeps the Score", 2015, 607_062);
        library.add("Jeff Kinney", "Rowley Jefferson’s Awesome Friendly Spooky Stories", 2021, 577_515);
        library.add("Taylor Jenkins Reid", "The Seven Husbands of Evelyn Hugo", 2018, 562_002);
        library.add("Dav Pilkey", "Grime and Punishment", 2020, 557_918);
        library.add("Amanda Gorman", "The Hill We Climb", 2021, 551_584);
        library.add("Bill Martin Jr.", "Brown Bear, Brown Bear, What Do You See?", 1996, 548_592);
        library.add("Jeff Kinney", "The Deep End", 2020, 543_624);
        library.add("Laura Dave", "The Last Thing He Told Me", 2021, 531_404);
        library.add("John Grisham", "The Judge’s List", 2021, 517_436);

        return library;
    }
}
