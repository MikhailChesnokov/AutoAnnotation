import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

class TokenProcessor {
    private final static Pattern stopWords = Pattern.compile(
            "(и|в|во|не|что|он|на|я|с|со|как|а|то|все|она|так|его|но|да|ты|к|у|ж|за|бы|по|только|ее|мне|было|вот|от|меня|еще|нет|о|из|ему|теперь|когда|даже|ну|" +
                    "вдруг|ли|если|уже|или|ни|быть|был|него|до|вас|нибудь|опять|уж|вам|сказал|ведь|там|потом|себя|ничего|ей|может|они|тут|где|есть|надо|ней|для|мы|тебя|их|" +
                    "чем|была|сам|чтоб|без|будто|человек|чего|раз|тоже|себе|под|жизнь|будет|тогда|кто|этот|говорил|того|потому|этого|какой|совсем|ним|здесь|этом|один|почти|" +
                    "мой|тем|чтобы|нее|кажется|сейчас|были|куда|зачем|сказать|всех|никогда|сегодня|можно|при|наконец|два|об|другой|хоть|после|над|больше|тот|через|эти|нас|про|" +
                    "всего|них|какая|разве|сказала|три|эту|моя|впрочем|хорошо|свою|этой|перед|иногда|лучше|чуть|том|нельзя|такой|им|более|всегда|конечно|всю|между)");
    final static Map<String,Integer> frequencies = new TreeMap<>();

    static void incrementTokenFrequency(String token) {
        frequencies.put(token, frequencies.containsKey(token) ? frequencies.get(token) + 1 : 1);
    }

    static boolean isNotStopWord(String token) {
        return !stopWords.matcher(token).matches();
    }
}