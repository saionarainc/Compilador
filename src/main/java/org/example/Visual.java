package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

public class Visual extends JFrame {
    private final JTextArea editor = new JTextArea();
    private final JTextArea mensagens = new JTextArea();

    public Visual() {
        super("Compilador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setResizable(false);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        criarBarraDeFerramentas();
        criarEspacoDeEdicao();
    }


    /**
     * Método cria o espaço da barra de ferramentas, define um tamanho padronizado para os botões
     * e os adiciona no espaço reservado
     */
    private void criarBarraDeFerramentas() {
        JToolBar ferramentas = new JToolBar();
        ferramentas.setFloatable(false);
        ferramentas.setPreferredSize(new Dimension(1500, 70));

        Dimension btnTamanho = new Dimension(100, 60);
        //mudar icon
        JButton btnNovo = criarBotao("Novo\n(CTRL-N)", KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK), btnTamanho, carregarIcone("Add2.png"), e -> actionNovo());
        ferramentas.add(btnNovo);

        JButton btnAbrir = criarBotao("Abrir\n(CTRL-O)", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK), btnTamanho, carregarIcone("open.png"), e -> actionAbrir());
        ferramentas.add(btnAbrir);

        JButton btnSalvar = criarBotao("Salvar\n(CTRL-S)", KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), btnTamanho, carregarIcone("save.png"), e -> actionSalvar());
        ferramentas.add(btnSalvar);

        JButton btnCopiar = criarBotao("Copiar\n(CTRL-C)", KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), btnTamanho, carregarIcone("Copy2.png"), e -> actionCopiar());
        ferramentas.add(btnCopiar);

        JButton btnColar = criarBotao("Colar\n(CTRL-V)", KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), btnTamanho, carregarIcone("note.png"), e -> actionColar());
        ferramentas.add(btnColar);

        JButton btnRecortar = criarBotao("Recortar\n(CTRL-X)", KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK), btnTamanho, carregarIcone("cutt.png"), e -> actionRecortar());
        ferramentas.add(btnRecortar);

        JButton btnCompilar = criarBotao("Compilar\n(F7)", KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), btnTamanho, carregarIcone("compilation.png"), e -> actionCompilar());
        ferramentas.add(btnCompilar);

        JButton btnEquipe = criarBotao("Equipe\n(F1)", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), btnTamanho, carregarIcone("team.png"), e -> actionEquipe());
        ferramentas.add(btnEquipe);

        this.add(ferramentas, BorderLayout.NORTH);
    }


    /**
     * Método para padronizar requisitos visuais esperados de um botão e padroniza comportamento
     * @param nome
     * @param ks
     * @param tamanho
     * @param icone
     * @param al
     * @return imagem do botão conforme o recebido por parâmetro
     */
    private JButton criarBotao (String nome, KeyStroke ks, Dimension tamanho, ImageIcon icone, ActionListener al) {
        JButton botao = new JButton(nome, icone);
        botao.addActionListener(al::actionPerformed);
        botao.setPreferredSize(tamanho);
        botao.setVerticalTextPosition(SwingConstants.BOTTOM);
        botao.setHorizontalTextPosition(SwingConstants.CENTER);
        botao.setBorderPainted(false);
        botao.setFocusable(true);

        String tecla = nome;
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ks, tecla);
        getRootPane().getActionMap().put(tecla, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botao.doClick();
            }
        });
        return botao;
    }

    /**
     * Faz a procura dentro da pasta resources e caso não encontre, chama criarImagem() para gerar um genérico
     * @param nome
     * @return imagem criada a partir dos ícones pngs na pasta resources/icons/
     */
    private ImageIcon carregarIcone(String nome) {
        try {
            URL url = Visual.class.getResource("/icons/" + nome);
            if (url != null) {
                ImageIcon icone = new ImageIcon(url);
                Image img = icone.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
            //se a criação do ícone falhar, o código ignora o erro e cria uma imagem genérica com a primeira letra do botão
        } catch (Exception e) {
        }
        return criarImagem(Color.blue, nome.substring(0, 1).toUpperCase());
    }

    /**
     * Método para gerar uma imagem genérica caso a criação da foto do botão falhe
     * @param color
     * @param letra
     * @return imagem arredondada 20x20 com a 1ª letra do botão centralizada em branco e negrito
     */
    private ImageIcon criarImagem(Color color, String letra) {
        BufferedImage img = new BufferedImage(45, 45, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        g.setColor(color);
        g.fillRoundRect(0, 0, 45, 45, 8, 8);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString(letra, 10, 10);
        return new ImageIcon(img);
    }

    private void criarEspacoDeEdicao() {
        editor.setFont(new Font("Arial", Font.PLAIN, 16));
        editor.setEditable(true);
        editor.setLineWrap(false);

        editor.setBorder(new NumberedBorder());
        JScrollPane espacoEdicao = new JScrollPane(editor);
        espacoEdicao.setBorder(BorderFactory.createCompoundBorder(new NumberedBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));
        espacoEdicao.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        espacoEdicao.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        mensagens.setEditable(false);
        mensagens.setFont(new Font("Arial", Font.PLAIN, 16));
        JScrollPane espacoMensagens = new JScrollPane(mensagens);
        espacoMensagens.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        espacoMensagens.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JSplitPane separacao = new JSplitPane(JSplitPane.VERTICAL_SPLIT, espacoEdicao, espacoMensagens);
        separacao.setOneTouchExpandable(true);
        separacao.setResizeWeight(0.7);
        separacao.setDividerSize(7);

        this.add(separacao, BorderLayout.CENTER);
    }

  
    private java.io.File arquivoAtual = null; 
    //Fazer funções 
    private void actionNovo() {
        editor.setText("");       
        mensagens.setText("");    
        mensagens.setText("Novo arquivo iniciado.\n");
    }

    private void actionAbrir() {
          JFileChooser fileChooser = new JFileChooser();
          fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos de Texto (*.txt)", "txt"));

        int opcao = fileChooser.showOpenDialog(this);

        if (opcao == JFileChooser.APPROVE_OPTION) {
         try {
            java.io.File arquivo = fileChooser.getSelectedFile();
            if (!arquivo.getName().toLowerCase().endsWith(".txt")) {
                mensagens.setText("Erro: apenas arquivos .txt podem ser abertos.\n");
                return;
            }

            String conteudo = java.nio.file.Files.readString(arquivo.toPath());

            editor.setText(conteudo);
            mensagens.setText(""); 
            //status4.setText("Arquivo aberto: " + arquivo.getAbsolutePath());

            arquivoAtual = arquivo;

        } catch (Exception e) {
            mensagens.setText("Erro ao abrir arquivo: " + e.getMessage());
            //status4.setText("Falha ao abrir arquivo.");
        }
    }
    }

    private void actionSalvar() {
        try {
        if (arquivoAtual == null) {
            //Arquivo novo
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos de Texto (*.txt)", "txt"));

            int opcao = fileChooser.showSaveDialog(this);
            if (opcao == JFileChooser.APPROVE_OPTION) {
                arquivoAtual = fileChooser.getSelectedFile();

                // Garante que a extensão seja .txt
                if (!arquivoAtual.getName().toLowerCase().endsWith(".txt")) {
                    arquivoAtual = new java.io.File(arquivoAtual.getAbsolutePath() + ".txt");
                }

                java.nio.file.Files.writeString(arquivoAtual.toPath(), editor.getText());

                mensagens.setText(""); // limpa mensagens2
                //status4.setText("Arquivo salvo em: " + arquivoAtual.getAbsolutePath());
            }

        } else {
            //Já existe arquivo aberto
            java.nio.file.Files.writeString(arquivoAtual.toPath(), editor.getText());
            mensagens.setText(""); // limpa mensagens2
            //status5.setText("Alterações salvas em: " + arquivoAtual.getAbsolutePath());
        }

    } catch (Exception e) {
        mensagens.setText("Erro ao salvar arquivo: " + e.getMessage());
        //status4.setText("Falha ao salvar arquivo.");
    }
        
    }
     //Feito
    private void actionCopiar() {
        String textoSelecionado = editor.getSelectedText();
        editor.copy(); 
    }
     //Feito
    private void actionColar() {
        int posicaoCursor = editor.getCaretPosition();
         editor.paste(); 
    }
     //Feito
    private void actionRecortar() { 
        String textoSelecionado = editor.getSelectedText();
        editor.cut();
    }
    //Feito
    private void actionCompilar() {
        mensagens.setText("Compilação de programas ainda não foi implementada.");
    }
     //Feito
    private void actionEquipe() {
        mensagens.setText("Equipe: Ana Carolina Fanderuff Mellek e Saionara Inácio");
    }

    public static void main(String[] args) {
        Visual compilador = new Visual();
        compilador.setVisible(true);
    }
}