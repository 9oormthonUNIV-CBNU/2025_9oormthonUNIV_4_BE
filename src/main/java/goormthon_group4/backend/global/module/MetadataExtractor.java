package goormthon_group4.backend.global.module;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MetadataExtractor {

  public static String extractTitle(String url) {
    try {
      Document doc = Jsoup.connect(url).get();

      // <title> 태그 우선
      String title = doc.title();
      if (title != null && !title.isEmpty()) {
        return title;
      }

      // og:title 대체
      Element ogTitle = doc.selectFirst("meta[property=og:title]");
      if (ogTitle != null && ogTitle.hasAttr("content")) {
        return ogTitle.attr("content");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return "제목 없음";
  }

  public static String extractImage(String url) {
    try {
      Document doc = Jsoup.connect(url).get();

      Element ogImage = doc.selectFirst("meta[property=og:image]");
      if (ogImage != null && ogImage.hasAttr("content")) {
        return ogImage.attr("content");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return "이미지 없음";
  }

  public static String extractFavicon(String url) {
    try {
      Document doc = Jsoup.connect(url).get();

      // favicon 또는 shortcut icon 찾기
      Element iconLink = doc.selectFirst("link[rel~=(?i)^(shortcut )?icon$]");
      if (iconLink != null && iconLink.hasAttr("href")) {
        return iconLink.attr("abs:href"); // 절대 경로로 반환
      }

      // 기본 fallback: /favicon.ico
      if (!url.endsWith("/")) {
        url += "/";
      }
      return url + "favicon.ico";

    } catch (Exception e) {
      e.printStackTrace();
    }
    return "파비콘 없음";
  }
}
