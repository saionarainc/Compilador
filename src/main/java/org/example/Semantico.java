package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Semantico implements Constants{
    private StringBuilder codigo;
    private Stack<String> pilhaTipos;
    private Stack<String> pilhaRotulos;
    private List<Token> listaIdentificadores;
    private Map<String, String> tabelaSimbolos;
    private String operadorRelacional;
    private String tipoAtual;                  
    private int proxRotulo;                    

    public Semantico() {
        this.codigo = new StringBuilder();
        this.pilhaTipos = new Stack<>();
        this.pilhaRotulos = new Stack<>();
        this.listaIdentificadores = new ArrayList<>();
        this.tabelaSimbolos = new HashMap<>();
        this.operadorRelacional = "";
        this.tipoAtual = "";
        this.proxRotulo = 0;
    }

    public String getCodigoObjeto() {
        return codigo.toString();
    }

    private void gera(String instrucao) {
        codigo.append(instrucao).append("\n");
    }

    private String novoRotulo() {
        return "L" + (proxRotulo++);
    }

    private String tipoFonteParaIL(String tipoFonte) {
        switch (tipoFonte) {
            case "int":    return "int64";
            case "float":  return "float64";
            case "string": return "string";
            case "bool":   return "bool";
            default:       return tipoFonte;
        }
    }

    private String resultadoAritmetico(String t1, String t2, String operadorIL) {
        if ("div".equals(operadorIL)) {
            return "float64";
        }
        if ("float64".equals(t1) || "float64".equals(t2)) {
            return "float64";
        }
        return "int64";
    }

    private String tipoRelacional(String t1, String t2, String op) {
        return "bool";
    }

    private String resultadoLogico(String t1, String t2, String op) {
        return "bool";
    }

    private String getTipoIdentificador(String nome, int posicao) throws SemanticError {
        String tipo = tabelaSimbolos.get(nome);
        if (tipo == null) {
            throw new SemanticError("identificador " + nome + " não declarado", posicao);
        }
        return tipo;
    }

    public void executeAction(int action, Token token) throws SemanticError {
        switch (action) {
            case 100: acao100(); break;
            case 101: acao101(); break;
            case 102: acao102(); break;
            case 103: acao103(token); break;
            case 104: acao104(token); break;
            case 105: acao105(token); break;
            case 106: acao106(); break;
            case 107: acao107(); break;
            case 108: acao108(); break;
            case 109: acao109(); break;
            case 110: acao110(); break;
            case 111: acao111(token); break;
            case 112: acao112(); break;
            case 113: acao113(); break;
            case 114: acao114(); break;
            case 115: acao115(); break;
            case 116: acao116(); break;
            case 117: acao117(); break;
            case 118: acao118(); break;
            case 119: acao119(); break;
            case 120: acao120(token); break;
            case 121: acao121(token); break;
            case 122: acao122(); break;
            case 123: acao123(token); break;
            case 124: acao124(token); break;
            case 125: acao125(token); break;
            case 126: acao126(); break;
            case 127: acao127(); break;
            case 128: acao128(); break;
            case 129: acao129(token); break;
            case 130: acao130(token); break;
            default:
                break;
        }
        
    }

    private void acao100() {
        gera(".assembly extern mscorlib {}");
        gera(".assembly _programa{}");
        gera(".module _programa.exe");
        gera(".class public _unica{");
        gera(" .method static public void _principal(){");
        gera(" .entrypoint");
    }

    private void acao101() {
        gera("// fim de programa");
        gera(" ret");
        gera(" }");
        gera("}");
    }

    private void acao102() {
        if (pilhaTipos.isEmpty()) return;

        String tipo = pilhaTipos.pop();

        if ("int64".equals(tipo)) {
            gera(" conv.i8");
        }

        gera(" call void [mscorlib]System.Console::Write(" + tipo + ")");
    }

    private void acao103(Token token) {
        pilhaTipos.push("int64");
        gera(" ldc.i8 " + token.getLexeme());
        gera(" conv.r8");
    }

    private void acao104(Token token) {
        pilhaTipos.push("float64");
        gera(" ldc.r8 " + token.getLexeme());
    }

    private void acao105(Token token) {
        pilhaTipos.push("string");
        gera(" ldstr " + token.getLexeme());
    }

    private void acao115() {
        pilhaTipos.push("bool");
        gera(" ldc.i4.1");
    }

    private void acao116() {
        pilhaTipos.push("bool");
        gera(" ldc.i4.0");
    }

    private void acao106() {
        String t2 = pilhaTipos.pop();
        String t1 = pilhaTipos.pop();
        String r = resultadoAritmetico(t1, t2, "add");
        pilhaTipos.push(r);
        gera(" add");
    }

    private void acao107() {
        String t2 = pilhaTipos.pop();
        String t1 = pilhaTipos.pop();
        String r = resultadoAritmetico(t1, t2, "sub");
        pilhaTipos.push(r);
        gera(" sub");
    }

    private void acao108() {
        String t2 = pilhaTipos.pop();
        String t1 = pilhaTipos.pop();
        String r = resultadoAritmetico(t1, t2, "mul");
        pilhaTipos.push(r);
        gera(" mul");
    }

    private void acao109() {
        String t2 = pilhaTipos.pop();
        String t1 = pilhaTipos.pop();
        String r = resultadoAritmetico(t1, t2, "div");
        pilhaTipos.push(r);
        gera(" div");
    }

    private void acao110() {
        gera(" neg");
    }

    private void acao111(Token token) {
        operadorRelacional = token.getLexeme(); // "==", "~=", "<" ou ">"
    }

    private void acao112() {
        String t2 = pilhaTipos.pop();
        String t1 = pilhaTipos.pop();
        String r = tipoRelacional(t1, t2, operadorRelacional);
        pilhaTipos.push(r);

        switch (operadorRelacional) {
            case "==":
                gera(" ceq");
                break;
            case "~=":
                gera(" ceq");
                gera(" ldc.i4.0");
                gera(" ceq");
                break;
            case "<":
                gera(" clt");
                break;
            case ">":
                gera(" cgt");
                break;
        }
    }

    private void acao113() {
        String t2 = pilhaTipos.pop();
        String t1 = pilhaTipos.pop();
        String r = resultadoLogico(t1, t2, "and");
        pilhaTipos.push(r); // bool
        gera(" and");
    }

    private void acao114() {
        String t2 = pilhaTipos.pop();
        String t1 = pilhaTipos.pop();
        String r = resultadoLogico(t1, t2, "or");
        pilhaTipos.push(r); // bool
        gera(" or");
    }

   
    private void acao117() {
        gera(" ldc.i4.0");
        gera(" ceq");
    }

    private void acao118() {
        gera(" ldstr \"\\n\"");
        gera(" call void [mscorlib]System.Console::Write(string)");
    }

    private void acao119() throws SemanticError {
        String tipoIL = tipoFonteParaIL(tipoAtual);

        for (Token idToken : listaIdentificadores) {
            String nome = idToken.getLexeme();

            if (tabelaSimbolos.containsKey(nome)) {
                throw new SemanticError("identificador " + nome + " já declarado",
                        idToken.getPosition());
            }

            tabelaSimbolos.put(nome, tipoIL);

            gera(" .locals(" + tipoIL + " " + nome + ")");
        }

        listaIdentificadores.clear();
    }

    private void acao120(Token token) {
        tipoAtual = token.getLexeme();
    }

    private void acao121(Token token) {
        // Caso especial: "id" está sendo usado como EXPRESSÃO,
        // não como comando (como no caso do: if lado ...)
        if (pilhaTipos.size() >= 0) {
            // empilha tipo do id como se fosse #130
            String id = token.getLexeme();
            String tipo = tabelaSimbolos.get(id);

            if (tipo != null) {
                pilhaTipos.push(tipo);
                gera(" ldloc " + id);

                if ("int64".equals(tipo)) {
                    gera(" conv.r8");
                }

                return; // NÃO trata como comando
            }
        }
        // MODO NORMAL (id em comando de atribuição)
        listaIdentificadores.add(token);
    }

    private void acao122() throws SemanticError {
        
        if (listaIdentificadores.isEmpty())
            return;

        // id que está recebendo o valor
        Token idToken = listaIdentificadores.get(listaIdentificadores.size() - 1);
        String id = idToken.getLexeme();

        // tipo da expressão
        String tipoExpr = pilhaTipos.pop();

        // conversão int64 → conv.i8 (esquema)
        if ("int64".equals(tipoExpr)) {
            gera(" conv.i8");
        }

        gera(" stloc " + id);

        listaIdentificadores.clear();
        
    }

    private void acao123(Token token) throws SemanticError {
        if (listaIdentificadores.isEmpty())
            return;

        token = listaIdentificadores.get(0);
        String id = token.getLexeme();

        String tipo = tabelaSimbolos.get(id);

        if (tipo == null) {
            throw new SemanticError("identificador " + id + " não declarado", token.getPosition());
        }

        if ("bool".equals(tipo)) {
            throw new SemanticError(id + " inválido para comando de entrada", token.getPosition());
        }

        // lê linha
        gera(" call string [mscorlib]System.Console::ReadLine()");

        // converte conforme tipo
        switch (tipo) {
            case "int64":
                gera(" call int64 [mscorlib]System.Int64::Parse(string)");
                break;

            case "float64":
                gera(" call float64 [mscorlib]System.Double::Parse(string)");
                break;

            case "string":
                // nada
                break;
        }

        gera(" stloc " + id);

        listaIdentificadores.clear();
    }

    private void acao124(Token token){
        // token contém a cte_string
        String texto = token.getLexeme();

        gera(" ldstr " + texto);
        gera(" call void [mscorlib]System.Console::Write(string)");
    }

    private void acao125(Token token) throws SemanticError {
        String tipo = pilhaTipos.pop();

        if (!"bool".equals(tipo)) {
            throw new SemanticError("expressão incompatível em comando de seleção",
                    token.getPosition());
        }

        String rot = novoRotulo();

        gera(" brfalse " + rot);

        pilhaRotulos.push(rot);
    }

    private void acao126(){
        String rot = pilhaRotulos.pop();

        gera(" " + rot + ":");
    }

    private void acao127(){
        String L2 = novoRotulo();     // fim do else
        String L1 = pilhaRotulos.pop(); // rótulo do brfalse

        gera(" br " + L2);
        gera(" " + L1 + ":");

        pilhaRotulos.push(L2);
    }

    private void acao128(){
        String rot = novoRotulo();

        gera(" " + rot + ":");

        pilhaRotulos.push(rot);
    }

    private void acao129(Token token) throws SemanticError {
        String tipo = pilhaTipos.pop();

        if (!"bool".equals(tipo)) {
            throw new SemanticError("expressão incompatível em comando de repetição",
                token.getPosition());
        }

        String rot = pilhaRotulos.pop();

        gera(" brfalse " + rot);
    }

    private void acao130(Token token) throws SemanticError {
        System.out.println("SEMANTICO FOI CHAMADO");
        String id = token.getLexeme();

        String tipo = tabelaSimbolos.get(id);

        if (tipo == null) {
            throw new SemanticError("identificador " + id + " não declarado",
                token.getPosition());
        }

        pilhaTipos.push(tipo);  // empilha o tipo do id

        gera(" ldloc " + id);   // gera código para carregar o valor

        if ("int64".equals(tipo)) {
            gera(" conv.r8");   // conversão conforme o PDF
        }
    }
}
