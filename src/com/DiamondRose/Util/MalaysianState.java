package com.DiamondRose.Util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

final public class MalaysianState{

	private static final Map<Integer, MalaysianState> STATES = new HashMap<>();

	static{
		MalaysianState.STATES.put(14, new MalaysianState(14, "Federal Territory of Kuala Lumpur"));
		MalaysianState.STATES.put(54, new MalaysianState(54, "Federal Territory of Kuala Lumpur"));
		MalaysianState.STATES.put(55, new MalaysianState(55, "Federal Territory of Kuala Lumpur"));
		MalaysianState.STATES.put(56, new MalaysianState(56, "Federal Territory of Kuala Lumpur"));
		MalaysianState.STATES.put(57, new MalaysianState(57, "Federal Territory of Kuala Lumpur"));
		MalaysianState.STATES.put(15, new MalaysianState(15, "Federal Territory of Labuan"));
		MalaysianState.STATES.put(58, new MalaysianState(58, "Federal Territory of Labuan"));
		MalaysianState.STATES.put(16, new MalaysianState(16, "Federal Territory of Putrajaya"));
		MalaysianState.STATES.put(1, new MalaysianState(1, "Johor"));
		MalaysianState.STATES.put(21, new MalaysianState(21, "Johor"));
		MalaysianState.STATES.put(22, new MalaysianState(22, "Johor"));
		MalaysianState.STATES.put(23, new MalaysianState(23, "Johor"));
		MalaysianState.STATES.put(24, new MalaysianState(24, "Johor"));
		MalaysianState.STATES.put(2, new MalaysianState(2, "Kedah"));
		MalaysianState.STATES.put(25, new MalaysianState(25, "Kedah"));
		MalaysianState.STATES.put(26, new MalaysianState(26, "Kedah"));
		MalaysianState.STATES.put(27, new MalaysianState(27, "Kedah"));
		MalaysianState.STATES.put(3, new MalaysianState(3, "Kelantan"));
		MalaysianState.STATES.put(28, new MalaysianState(28, "Kelantan"));
		MalaysianState.STATES.put(29, new MalaysianState(29, "Kelantan"));
		MalaysianState.STATES.put(4, new MalaysianState(4, "Malacca"));
		MalaysianState.STATES.put(30, new MalaysianState(30, "Malacca"));
		MalaysianState.STATES.put(5, new MalaysianState(5, "Negeri Sembilan"));
		MalaysianState.STATES.put(31, new MalaysianState(31, "Negeri Sembilan"));
		MalaysianState.STATES.put(59, new MalaysianState(59, "Negeri Sembilan"));
		MalaysianState.STATES.put(6, new MalaysianState(6, "Pahang"));
		MalaysianState.STATES.put(32, new MalaysianState(32, "Pahang"));
		MalaysianState.STATES.put(33, new MalaysianState(33, "Pahang"));
		MalaysianState.STATES.put(7, new MalaysianState(7, "Penang"));
		MalaysianState.STATES.put(34, new MalaysianState(34, "Penang"));
		MalaysianState.STATES.put(35, new MalaysianState(35, "Penang"));
		MalaysianState.STATES.put(8, new MalaysianState(8, "Perak"));
		MalaysianState.STATES.put(36, new MalaysianState(36, "Perak"));
		MalaysianState.STATES.put(37, new MalaysianState(37, "Perak"));
		MalaysianState.STATES.put(38, new MalaysianState(38, "Perak"));
		MalaysianState.STATES.put(39, new MalaysianState(39, "Perak"));
		MalaysianState.STATES.put(9, new MalaysianState(9, "Perlis"));
		MalaysianState.STATES.put(40, new MalaysianState(40, "Perlis"));
		MalaysianState.STATES.put(12, new MalaysianState(12, "Sabah"));
		MalaysianState.STATES.put(47, new MalaysianState(47, "Sabah"));
		MalaysianState.STATES.put(48, new MalaysianState(48, "Sabah"));
		MalaysianState.STATES.put(49, new MalaysianState(49, "Sabah"));
		MalaysianState.STATES.put(13, new MalaysianState(13, "Sarawak"));
		MalaysianState.STATES.put(50, new MalaysianState(50, "Sarawak"));
		MalaysianState.STATES.put(51, new MalaysianState(51, "Sarawak"));
		MalaysianState.STATES.put(52, new MalaysianState(52, "Sarawak"));
		MalaysianState.STATES.put(53, new MalaysianState(53, "Sarawak"));
		MalaysianState.STATES.put(10, new MalaysianState(10, "Selangor"));
		MalaysianState.STATES.put(41, new MalaysianState(41, "Selangor"));
		MalaysianState.STATES.put(42, new MalaysianState(42, "Selangor"));
		MalaysianState.STATES.put(43, new MalaysianState(43, "Selangor"));
		MalaysianState.STATES.put(44, new MalaysianState(44, "Selangor"));
		MalaysianState.STATES.put(11, new MalaysianState(11, "Terengganu"));
		MalaysianState.STATES.put(45, new MalaysianState(45, "Terengganu"));
		MalaysianState.STATES.put(46, new MalaysianState(46, "Terengganu"));
	}

	public static Collection<MalaysianState> getAll(){
		return MalaysianState.STATES.values();
	}

	public static MalaysianState fromCode(int code){
		MalaysianState state = MalaysianState.STATES.get(code);
		if(state == null){
			throw new IllegalArgumentException("Invalid Malaysian State Code: " + code);
		}

		return state;
	}

	public static MalaysianState random(){
		return MalaysianState.STATES.values().stream().toList().get(new Random().nextInt(MalaysianState.STATES.size()));
	}

	private final int code;
	private final String name;

	private MalaysianState(int code, String name){
		this.code = code;
		this.name = name;
	}

	public int getCode(){
		return this.code;
	}

	public String getName(){
		return this.name;
	}
}
