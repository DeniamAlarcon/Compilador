
import java.util.HashMap;
import java.util.Map;

public class Token {
    private Map<Integer, String> tokenMap;

    public Token() {
        tokenMap = new HashMap<>();
        // Agrega los nombres de los tokens al mapa
        tokenMap.put(1, "Token0");
        tokenMap.put(2, "Token1");
        tokenMap.put(3, "Token2");
        tokenMap.put(4, "Token4");
        // Agrega más tokens según sea necesario
    }

    public String getTokenName(int token) {
        if (tokenMap.containsKey(token)) {
            return tokenMap.get(token);
        } else {
            return "Token Desconocido";
        }
    }
}
