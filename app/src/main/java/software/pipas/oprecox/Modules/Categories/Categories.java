package software.pipas.oprecox.Modules.Categories;

import java.util.ArrayList;
import java.util.Random;

import static android.R.id.input;

/**
 * Created by Pipas_ on 20/03/2017.
 */

public class Categories
{
    ArrayList<String> selected = new ArrayList<>();

    public String getName(int ID)
    {
        switch (ID)
        {
            case 1:
                return "Acessórios";
            case 2:
                return "Animais";
            case 3:
                return "Roupa";
            case 4:
                return "Calçado";
            case 5:
                return "Outros artigos";
            case 6:
                return "Carros";
            case 7:
                return "Peças";
            case 8:
                return "Outros veículos";
            case 9:
                return "Artigos desportivos";
            case 10:
                return "Bicicletas";
            case 11:
                return "Apartamentos";
            case 12:
                return "Moradias ou Casas";
            case 13:
                return "Outros imóveis";
            case 14:
                return "Brinquedos ou jogos";
            case 15:
                return "Instrumentos musicais";
            case 16:
                return "Livros ou Revistas";
            case 17:
                return "Colecções ou Antiguidades";
            case 18:
                return "DVDs ou Filmes";
            case 19:
                return "CDs, Vinis ou Música";
            case 20:
                return "Roupa";
            case 21:
                return "Calçado";
            case 22:
                return "Joias, Relógios e Bijuteria";
            case 23:
                return "Malas e Acessórios";
            case 24:
                return "Saúde e Beleza";
            case 25:
                return "Utilidades e Decoração";
            case 26:
                return "Electrodomésticos";
            case 27:
                return "Jardim e Bricolage";
            case 28:
                return "Móveis";
            case 29:
                return "Videojogos ou Consolas";
            case 30:
                return "Computadores ou Informática";
            case 31:
                return "Electrónica";
            case 32:
                return "TV, Som e Fotografia";
            case 33:
                return "Acessórios";
            case 34:
                return "Telemóveis ou Tablets";
            case 35:
                return "Outras Vendas";
            default:
                break;
        }
        return null;
    }

    public String getURLEnd(int ID)
    {
        switch (ID)
        {
            case 1:
                return "animais/acessorios-animais";
            case 2:
            {
                Random rand = new Random();
                int randomNumber = rand.nextInt(10418) + 1;
                if(randomNumber < 907)
                    return "animais/animais-de-quinta";
                else if(randomNumber >= 907 && randomNumber < 919)
                    return "animais/aranhas";
                else if(randomNumber >= 919 && randomNumber < 5158)
                    return "animais/aves";
                else if(randomNumber >= 5158 && randomNumber < 5812)
                    return "animais/cavalos";
                else if(randomNumber >= 5812 && randomNumber < 8516)
                    return "animais/caes";
                else if(randomNumber >= 8516 && randomNumber < 8673)
                    return "animais/gatos";
                else if(randomNumber >= 8673 && randomNumber < 8786)
                    return "animais/insectos";
                else if(randomNumber >= 8786 && randomNumber < 9673)
                    return "animais/peixes";
                else if(randomNumber >= 9673 && randomNumber < 10245)
                    return "animais/roedores";
                else if(randomNumber >= 10245)
                    return "animais/repteis";
            }
            case 3:
                return "bebes-criancas/roupinhas";
            case 4:
                return "bebes-criancas/calcado-bebes";
            case 5:
            {
                Random rand = new Random();
                int randomNumber = rand.nextInt(44292) + 1;
                if(randomNumber < 10156)
                    return "bebes-criancas/passeio";
                else if(randomNumber >= 10156 && randomNumber < 17911)
                    return "bebes-criancas/refeicao";
                else if(randomNumber >= 17911 && randomNumber < 36562)
                    return "bebes-criancas/relaxar-e-dormir";
                else if(randomNumber >= 36562)
                    return "bebes-criancas/seguranca";
            }
            case 6:
                return "carros-motos-e-barcos/carros";
            case 7:
                return "carros-motos-e-barcos/pecas-e-acessorios-carros";
            case 8:
            {
                Random rand = new Random();
                int randomNumber = rand.nextInt(16990) + 1;
                if(randomNumber < 5923)
                    return "carros-motos-e-barcos/motociclos-scooters";
                else if(randomNumber >= 5923 && randomNumber < 12635)
                    return "carros-motos-e-barcos/camioes-veiculos-comerciais";
                else if(randomNumber >= 12635 && randomNumber < 13432)
                    return "carros-motos-e-barcos/barcos-lanchas";
                else if(randomNumber >= 13432 && randomNumber < 15092)
                    return "carros-motos-e-barcos/autocaravanas-roulotes-reboques";
                else if(randomNumber >= 15092 && randomNumber < 16504)
                    return "carros-motos-e-barcos/outros-veiculos";
                else if(randomNumber >= 16504)
                    return "carros-motos-e-barcos/salvados";
            }
            case 9:
                return "desporto-e-lazer/artigos-desportivos";
            case 10:
                return "desporto-e-lazer/bicicletas";
            case 11:
                return "imoveis/apartamento-casa-a-venda";
            case 12:
                return "imoveis/casas-moradias-para-arrendar-vender";
            case 13:
            {
                Random rand = new Random();
                int randomNumber = rand.nextInt(111597) + 1;
                if(randomNumber < 3185)
                    return "imoveis/quartos-para-aluguer";
                else if(randomNumber >= 3185 && randomNumber < 6524)
                    return "imoveis/casas-de-ferias";
                else if(randomNumber >= 6524 && randomNumber < 10020)
                    return "imoveis/permutas";
                else if(randomNumber >= 10020 && randomNumber < 14243)
                    return "imoveis/garagens-estacionamento";
                else if(randomNumber >= 14243 && randomNumber < 65739)
                    return "imoveis/terrenos-quintas";
                else if(randomNumber >= 65739 && randomNumber < 97915)
                    return "imoveis/escritorios-lojas";
                else if(randomNumber >= 97915)
                    return "imoveis/estabelecimentos-comerciais-para-alugar-vender";
            }
            case 14:
                return "lazer/jogos-brinquedos";
            case 15:
                return "lazer/instrumentos-musicais";
            case 16:
                return "lazer/livros-revistas";
            case 17:
                return "lazer/coleccoes-antiguidades";
            case 18:
                return "lazer/dvd-filmes";
            case 19:
                return "lazer/discos-vinil-cds-musica";
            case 20:
                return "moda/roupa-moda";
            case 21:
                return "moda/calcado";
            case 22:
                return "moda/joias-bijuteria-relogios";
            case 23:
                return "moda/malas-e-acessorios";
            case 24:
                return "moda/saude-beleza";
            case 25:
                return "moveis-casa-e-jardim/utilidades-e-decoracao";
            case 26:
                return "moveis-casa-e-jardim/electrodomesticos";
            case 27:
                return "moveis-casa-e-jardim/jardim-e-bricolage";
            case 28:
                return "moveis-casa-e-jardim/moveis-decoracao";
            case 29:
                return "tecnologia-e-informatica/videojogos-consolas";
            case 30:
                return "tecnologia-e-informatica/computadores-informatica";
            case 31:
                return "tecnologia-e-informatica/electronica";
            case 32:
                return "tecnologia-e-informatica/fotografia-tv-som";
            case 33:
                return "telemoveis-e-tablets/acessorios-telemoveis-tablets";
            case 34:
            {
                Random rand = new Random();
                int randomNumber = rand.nextInt(37845) + 1;
                if(randomNumber < 34124)
                    return "telemoveis-e-tablets/telemoveis";
                else
                    return "telemoveis-e-tablets/tablets";
            }
            case 35:
                return "compra-venda/o-resto";
            default:
                break;
        }
        return null;
    }

    public boolean checkSelected(int ID)
    {
        return selected.contains(Integer.toString(ID));
    }

    public boolean checkAllSelected()
    {
        for(int i = 1; i < 36; i++)
            if(!checkSelected(i))
                return false;

        return true;
    }

    public String returnIfSelected(int ID, String tooltip)
    {
        if(checkSelected(ID))
        {
            if(tooltip.equals(""))
            {
                return getName(ID);
            }
            else
                return ", " + getName(ID);
        }
        return "";
    }

    public void add(int ID)
    {
        if(!checkSelected(ID))
            selected.add(Integer.toString(ID));
    }

    public void remove(int ID)
    {
        if(checkSelected(ID))
            selected.remove(selected.indexOf(Integer.toString(ID)));
    }

    public String generateURL()
    {
        Random rand = new Random();
        int categoryNumber = rand.nextInt(selected.size());
        return getURLEnd(Integer.parseInt(selected.get(categoryNumber)));
    }

    public ArrayList<String> getSelected()
    {
        return selected;
    }

    public void setSelected(ArrayList<String> s)
    {
        selected = s;
    }

    public void selectAll()
    {
        for(int i = 1; i < 36; i++)
            add(i);
    }

    @Override
    public String toString()
    {
        String s = "";
        for (int i = 0; i < selected.size(); i++)
        {
            s += selected.get(i) + " ";
        }
        return s;
    }

    public void selectFromString(String input)
    {
        if(input.isEmpty())
            return;
        String[] elements = input.split(" ");
        selected.clear();
        for(int i = 0; i < elements.length; i++)
        {
            selected.add(elements[i]);
        }
    }
}
