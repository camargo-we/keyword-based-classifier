package example;

import classifier.Class;
import classifier.Document;

import java.util.HashSet;
import java.util.Set;

public class Data {

    public static Set<Class> getPurposeClasses() {
        Set<Class> classes = new HashSet<>();
        ClassImpl teaching = new ClassImpl("teaching",	"Teaching",	"12.363.1039.5292; 12.364.1043.5304; 12.364.1043.5305; 5292; 5304; 5305; Ensino; didático; pedagógico; aula; aluno; estudante; docência; estágio; departamento; ministrar; prática; curso; graduação; pós-graduação; mestrado; doutorado; conselho; colegiado; via de; diploma; histórico; PROGRAD; PROPG; COPE; Guia de recebimento; Portaria APLO; PAPEL ALMACO; DOCENTES; EDUCAÇÃO TUTORIAL");
        classes.add(teaching);
        ClassImpl research = new ClassImpl("research",	"Research",	"12.364.1043.5305; 5305; 3.3.90.20.01; PROTÓTIPO; Para Pesquisa; Pesquisa em; Pesquisador; PROPe; projeto; laboratório; campo; experiência; experimento; artigo; taxa de; inscrição; publicação; evento; PROPE");
        classes.add(research);
        ClassImpl extension = new ClassImpl("extension",	"University Extension",	"12.392.1043.5306; 5306; Extensão; projeto; comunidade; atendimento; público; organização; evento; semana da; encontro; PROEX");
        classes.add(extension);
        ClassImpl administrative = new ClassImpl("administrative",	"Administrative management",	"12.122.0100.5272; 10.302.1042.5274; 5272; 5274; Administração; Conta de adiantamento; adiantamento; adm; uso no; uso na; seção; departamento; seguro; seguro de; consumo; correio; correspondencia; rotina; atividades; adiantamento; estagiário; planejamento; reunião; CADE; PROPEG; recisão; ressarcimento; do vale; Portaria APLO de Transferência; SECRETARIA DO; ENCONTRO DE ; RECOLHIMENTO DA RETENÇÃO; OITIVA; DEVOLUÇÃO; RESCISÃO CONTRATUAL; CARIMBO;REMANEJAMENTO/ESTORNO - LANÇAMENTO INTERNO");
        classes.add(administrative);
        ClassImpl infrastructure = new ClassImpl("infrastructure",	"Infrastructure provision",	"12.364.1043.1151; 12.126.1043.5313; 1151; 5313; Infraestrutura; estrutura; obra; reforma; adequação; manutenção; conserto; porta; janela; chave; chaveiro; telhado; piso; parede; APLO; portas; chaves; serviço; fechadura; de chave; reparo; emergencial; SERVIÇOS E PEÇAS DE CHAVEIRO");
        classes.add(infrastructure);
        return classes;
    }

    public static Set<Document> getDocuments() {
        Set<Document> documents = new HashSet<>();
        Document docTeaching1 = new DocumentImpl("Contratação de estagiário na modalidade Estágio Supervisionado em Docência na disciplina Administração", "12.364.1043.5304 - ensino de graduação nas universidades estaduais 3.3.90.18.03 - aux. finan. a estudantes conced. p/ univ. paulist Contratação de estagiário na modalidade Estágio Supervisionado em Docência na disciplina Administração. Estágio Supervisionado em Docência para o(a) Aluno(a): ******* ****** ****** *******, departamento de engenharia de produção, período 31/07 a 14/12/19, 8h/a semanais. forma de liberação: mediante crédito em conta no banco do brasil. despesas autorizadas pelo ordenador da despesa no processo em uso. foram atendidas às disposições do decreto estadual n° 64.078, de 21 de janeiro de 2019 - doe de 22 de janeiro de 2019. despesas autorizadas pelo ordenador da despesa no processo em uso.");
        documents.add(docTeaching1);
        Document docTeaching2 = new DocumentImpl("Conta de Adiantamento: de 1 a 31 de outubro/2019", "12.364.1043.5304 - ensino de graduação nas universidades estaduais 3.3.90.33.45 - outras despesas com transportes e locomoção adiantamento outras despesas com transporte e locomoção outubro/2019. Conta de Adiantamento: de 1 a 31 de outubro/2019. Processo: ***/2019. Responsável: ****** ***** *********. Classificação Econômica: 3.3.90.33.45 - outras despesas com transportes e locomoção. Empenho: *****/*****/2019 . Adiantamento: Conta de Adiantamento");
        documents.add(docTeaching2);
        Document docAdministrative = new DocumentImpl("Remanejamento/Estorno - Lançamento interno", "solicitação de itens do almoxarifado (nº *********) realizada por ******* ****** ********. - produto: recados post it pequeno. quantidade: -1 pacote 4,00 unidade. valor unitário: r$ 4,26. - produto: recados post it pequeno. quantidade: -2.000 pacote 4,00 unidade. valor unitário: r$ 3,77. - produto: recados post it grande. quantidade: -1 unidade . valor unitário: r$ 3,57. - produto: recados post it grande. quantidade: -2.000 unidade . valor unitário: r$ 3,46. - produto: papel sulfite, branco, a4, 75g. quantidade: -1.000 pacote 500.00 folha. valor unitário: r$ 14,06. - produto: grampo trilho. quantidade: -4.000 pacote 50.00 unidade. valor unitário: r$ 7,98. - produto: fita adesiva de papelaria,polipropileno,medindo(50mmx50m),transparente. quantidade: -1 unidade . valor unitário: r$ 2,54. - produto: clips 6/0. quantidade: -2.000 caixa 50.00 unidade. valor unitário: r$ 2,33. - produto: caixa p/ arquivo,pp corrugado,(350x250x130)mm,azul. quantidade: -1 unidade . valor unitário: r$ 4,17. - produto: caixa p/ arquivo,pp corrugado,(350x250x130)mm,azul. quantidade: -9.000 unidade . valor unitário: r$ 3,27. Origem do recurso: - Área: Área de Protocolo - Fonte de Recurso: Diretoria - Tesouro depósito 00.000.0000.0000 - Remanejamento Interno");
        documents.add(docAdministrative);
        Document docResearch = new DocumentImpl("Despesas com Auxílio Financeiro a Pesquisador - 2019", "3.3.90.20.01 - auxílio financeiro a pesq. - pesquisa individual despesas com auxilio financeiro a pesquisador 2019. auxílio financeiro a pesquisador - prof. ****** ******* ********* (dep), conforme solicitação de empenho anexa (ofício nº 00/2019 FEB). foram atendidas às disposições do decreto estadual n° 64.078, de 21 de janeiro de 2019 - doe de 22 de janeiro de 2019. despesas autorizadas pelo ordenador da despesa no processo em uso.");
        documents.add(docResearch);
        return documents;
    }



}

