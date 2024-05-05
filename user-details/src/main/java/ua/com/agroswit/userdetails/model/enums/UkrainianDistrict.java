package ua.com.agroswit.userdetails.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UkrainianDistrict {
    VINNYTSIA("Вінницька область"),
    VOLYN("Волинська область"),
    DNIPROPETROVSK("Дніпропетровська область"),
    DONETSK("Донецька область"),
    ZHYTOMYR("Житомирська область"),
    ZAKARPATTIA("Закарпатська область"),
    ZAPORIZHIA("Запорізька область"),
    IVANO_FRANKIVSK("Івано-Франківська область"),
    KYIV("Київська область"),
    KYIV_CITY("м. Київ"),
    KIROVOHRAD("Кіровоградська область"),
    LUGANSK("Луганська область"),
    LVIV("Львівська область"),
    MYKOLAIV("Миколаївська область"),
    ODESA("Одеська область"),
    POLTAVA("Полтавська область"),
    RIVNE("Рівненська область"),
    SUMY("Сумська область"),
    TERNOPIL("Тернопільська область"),
    KHARKIV("Харківська область"),
    KHERSON("Херсонська область"),
    KHMELNYTSKY("Хмельницька область"),
    CHERKASY("Черкаська область"),
    CHERNIHIV("Чернігівська область"),
    CHERNIVTSI("Чернівецька область");

    private final String districtName;
}
