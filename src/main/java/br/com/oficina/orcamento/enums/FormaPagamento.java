package br.com.oficina.orcamento.enums;

/**
 * Define as formas de pagamento e sinaliza se há desconto ou taxa.
 */
public enum FormaPagamento {
    DINHEIRO(true,  false),
    PIX     (true,  false),
    DEBITO  (false, false),
    CREDITO_A_VISTA   (false, false),
    CREDITO_PARCELADO (false, true);

    /** Indica se o pagamento tem desconto */
    private final boolean comDesconto;
    /** Indica se o pagamento tem algum tipo de taxa */
    private final boolean comTaxa;

    FormaPagamento(boolean comDesconto, boolean comTaxa) {
        this.comDesconto = comDesconto;
        this.comTaxa      = comTaxa;
    }

    public boolean isComDesconto() { return comDesconto; }
    public boolean isComTaxa()      { return comTaxa; }

    /**
     * Retorna o nome “bonitinho”:
     * ex: CREDITO_A_VISTA → Crédito A Vista
     */
    @Override
    public String toString() {
        // primeiro transforma underscored em espaços e tudo em lowercase
        String raw = name().replace('_',' ').toLowerCase();
        // depois capitaliza a primeira letra de cada palavra
        StringBuilder sb = new StringBuilder(raw.length());
        for (String palavra : raw.split(" ")) {
            if (palavra.isEmpty()) continue;
            sb.append(Character.toUpperCase(palavra.charAt(0)))
                    .append(palavra.substring(1))
                    .append(' ');
        }
        return sb.toString().trim();
    }
}
