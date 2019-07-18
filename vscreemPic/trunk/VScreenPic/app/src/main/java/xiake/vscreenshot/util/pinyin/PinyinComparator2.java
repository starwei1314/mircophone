package xiake.vscreenshot.util.pinyin;


import android.util.Log;

import java.util.Comparator;

import xiake.db.entity.AliFriend;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator2 implements Comparator<AliFriend> {


	@Override
	public int compare(AliFriend friend1, AliFriend friend2) {
		String nickName1 = friend1.getNick();
		String nickName2 = friend2.getNick();
		Log.e("compare: " ,"\n" +nickName1+"\n"+nickName2);
		CharacterParser characterParser = new CharacterParser();
		if (characterParser.getSortLetters(nickName1).equals("@")
				|| characterParser.getSortLetters(nickName2).equals("#")) {
			return -1;
		} else if (characterParser.getSortLetters(nickName1).equals("#")
				|| characterParser.getSortLetters(nickName2).equals("@")) {
			return 1;
		} else {
			return characterParser.getSortLetters(nickName1).compareTo(characterParser.getSortLetters(nickName2));
		}
	}
}
