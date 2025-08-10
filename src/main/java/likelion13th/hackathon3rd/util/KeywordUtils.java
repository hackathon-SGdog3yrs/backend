package likelion13th.hackathon3rd.util;

import java.util.*;
import java.util.regex.Pattern;

public class KeywordUtils {

    private static final Set<String> STOPWORDS = new HashSet<>(Arrays.asList(
            "그리고","그러면","그러나","하지만","또","또는","및",
            "저는","나는","내가","제가","우리는","너는","당신은",
            "이거","그거","저거","거기","여기","저기",
            "오늘","내일","어제","주말","주말에","이번","다음","지난",
            "에서","으로","으로서","에게","한테","보다","보다가","부터","까지","만",
            "은","는","이","가","을","를","에","의","와","과","도","로","랑","랑은",
            "좀","너무","아주","매우","진짜","정말","그냥","혹시",
            "하고","하려고","싶어요","싶다","같아요","같다"
    ));

    private static final Pattern TOKEN = Pattern.compile("[^ㄱ-ㅎ가-힣A-Za-z0-9]+");

    private static String stripParticles(String word) {
        return word.replaceAll("(은|는|이|가|을|를|에|의|와|과)$", "");
    }

    public static String pickKeywordFallback(String text) {
        if (text == null) return "";
        String cleaned = TOKEN.matcher(text).replaceAll(" ").trim().toLowerCase(Locale.ROOT);
        String[] toks = cleaned.split("\\s+");
        if (toks.length == 0) return "";

        for (String t : toks) {
            String base = stripParticles(t);
            if (base.length() >= 2 && !STOPWORDS.contains(base)) return base;
        }
        for (String t : toks) {
            String base = stripParticles(t);
            if (base.length() >= 2) return base;
        }
        return stripParticles(toks[0]);
    }

    public static String sanitizeModelKeyword(String modelKw, String originalText) {
        if (modelKw == null) return pickKeywordFallback(originalText);
        String k = modelKw.trim().toLowerCase(Locale.ROOT);
        k = TOKEN.matcher(k).replaceAll("");
        k = stripParticles(k);
        if (k.length() < 2 || STOPWORDS.contains(k)) {
            return pickKeywordFallback(originalText);
        }
        return k;
    }
}