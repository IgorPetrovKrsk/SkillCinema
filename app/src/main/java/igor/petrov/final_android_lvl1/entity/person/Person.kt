package igor.petrov.final_android_lvl1.entity.person

interface Person{
    val personId:Int
    val webUrl:String?
    val nameRu:String?
    val nameEn:String?
    val sex:String?
    val posterUrl: String?
    val growth:Int?
    val birthday:String?
    val death:String?
    val age:Int?
    val birthplace:String?
    val deathplace:String?
    //val spouses: List<Person>?
    val hasAwards:Int?
    val profession:String?
    val films:List<PersonFilm>?
}
