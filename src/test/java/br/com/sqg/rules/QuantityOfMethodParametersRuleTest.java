package br.com.sqg.rules;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import br.com.sqg.exception.NotStartOrEndWithParenthesesException;


public class QuantityOfMethodParametersRuleTest {

	@Test(expected = NotStartOrEndWithParenthesesException.class)
	public void shouldThrowAnExceptionIfNotStartOrEndWithParentheses() {
		String method = "void test() {  }";
		new QuantityOfMethodParametersRule(0).quantityOfParameters(method);
	}
	
	@Test
	public void shouldIdentifyNoParameters() {
		String method = "()";
		int quantity = new QuantityOfMethodParametersRule(0).quantityOfParameters(method);
		Assert.assertEquals(0, quantity);
	}
	
	@Test
	public void shouldIdentifyOneParameter() {
		String method = "(String a)";
		int quantity = new QuantityOfMethodParametersRule(0).quantityOfParameters(method);
		Assert.assertEquals(1, quantity);
	}
	
	@Test
	public void shouldIdentifyTwoParameter() {
		String method = "(String a, String b)";
		int quantity = new QuantityOfMethodParametersRule(0).quantityOfParameters(method);
		Assert.assertEquals(2, quantity);
	}
	
	@Test
	public void shouldExtractOnePArenthesesBlock() {
		String method = "void method() {  }";
		List<String> parameters = new QuantityOfMethodParametersRule(0).extractParenthesesBlock(method);
		Assert.assertEquals(1, parameters.size());
	}
	
	@Test
	public void shouldExtractOneMethodParameter() {
		String method = "void method(String a) {  }";
		List<String> parameters = new QuantityOfMethodParametersRule(0).extractParenthesesBlock(method);
		Assert.assertEquals(1, parameters.size());
		Assert.assertEquals("(String a)", parameters.get(0));
	}
	
	@Test
	public void shouldExtractMethodWithTwoParameters() {
		String method = "void other(String a, String b) {  }";
		List<String> parameters = new QuantityOfMethodParametersRule(0).extractParenthesesBlock(method);
		Assert.assertEquals(1, parameters.size());
		Assert.assertTrue(parameters.contains("(String a, String b)"));
	}
	
	@Test
	public void shouldExtractTwoParenthesesBlock() {
		String method = "();  }    private Map<String, ReportContasPagarBirtApropFin> apropriacaoFinanceiraMap;  private Map<String, ReportContasPagarBirtApropObra> apropriacaoObraMap;  private Map<Integer, ReportContasPagarBirtApropDepart> apropriacaoDepartMap;  private Map<String, ReportContasPagarBirtImpostoRetido> impostoRetidoMap;  private Map<String, ReportContasPagarBirtClassesCredor> classesCredorMap;    private Boolean ehChequeEmitidoNaoConciliado;    public Integer cdIndexadorNeutro;    public ReportContasPagarBirtInfoPagto infoPagto;    public Integer codigoGrupoEmpresa;  public String nomeGrupoEmpresa;    public Integer codigoEmpresa;  public String nomeEmpresa;    public Integer codigoSubsidiaria;  public String nomeSubsidiaria;    public Integer codigoHolding;  public String nomeHolding;    public Integer codigoAreaNegocio;  public String descricaoAreaNeogocio;    public Integer codigoProjeto;  public String nomeProjeto;    public Integer codigoTipoNegocio;  public String nomeTipoNegocio;    public String codigoDocumento;  public String descricaoDocumento;  public String numeroDocumento;  public String flagDocumentoPrevisao;  public String flagDocumentoRateioImposto;    public String dataVencimentoOriginal;  public String dataVencimento;  public String dataEmissao;  public String dataCompetencia;  public String dataContabil;    public Integer numeroTitulo;  public String codigoOrigemTitulo;  public Integer numeroParcela;  public Character flagSituacaoParcela;  public Character flagAutorizacaoParcela;    public Integer codigoCredor;  public String nomeCredor;  public String nomeFantasiaCredor;  public String numeroCPFCredor;  public String numeroCNPJCredor;  public String classesCredor;    public String observacaoTitulo;  public String usuarioCadastroTitulo;  public String dataCadastroTitulo;  public String usuarioAlteracaoTitulo;  public String dataAlteracaoTitulo;    public Integer codigoIndexador;  public String descricaoIndexadorParcela;  public String flagPeriodicidade;  public String dataBase;  public Integer quantidadeTotalDeParcelas;   public Double valorValidacao;  public Double valorOriginal;  public Double valorSaldoDevedor;  public Double valorMulta;  public Double valorJuros;  public Double valorCorrecaoMonetaria;    public Double valorDescontoPagamentoEscritural;  public Double valorDescontoTitulo;    public Double valorAcrescimoPagamentoEscritural;  public Double valorJurosImp;    public String numeroCheque;  public String dataCheque;      private String chaveClassesCredor(Integer cdCredor, Integer cdClasse)";
		List<String> parameters = new QuantityOfMethodParametersRule(0).extractParenthesesBlock(method);
		Assert.assertEquals(2, parameters.size());
		Assert.assertTrue(parameters.contains("()"));
		Assert.assertTrue(parameters.contains("(Integer cdCredor, Integer cdClasse)"));
	}
	
	@Test
	public void shouldReturnZeroToMethodCallWithString() {
		String method = "(\"string\" +      \"string\" +      \"string\" +      \"string string string \" +      \"string string string\")";
		int quantity = new QuantityOfMethodParametersRule(0).quantityOfParameters(method);
		Assert.assertEquals(0, quantity);
	}
	
	@Test
	public void shouldReturnZeroToMethodCall() {
		String method = "(a, b, c, d)";
		int quantity = new QuantityOfMethodParametersRule(0).quantityOfParameters(method);
		Assert.assertEquals(0, quantity);
	}
}
