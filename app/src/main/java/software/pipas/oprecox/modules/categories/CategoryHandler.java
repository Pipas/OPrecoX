package software.pipas.oprecox.modules.categories;

import java.util.ArrayList;
import java.util.Random;

import software.pipas.oprecox.R;

public abstract class CategoryHandler
{
    private static ArrayList<ParentCategory> categories = new ArrayList<>();


    public static boolean checkAllSelected()
    {
        for(ParentCategory parentCategory : categories)
            if(!parentCategory.isSelected())
                return false;

        return true;
    }

    public static String generateURL()
    {
        ArrayList<SubCategory> selectedSubCategories = new ArrayList<>();
        for(ParentCategory parentCategory : categories)
        {
            if(parentCategory.isSelected())
            {
                for(SubCategory subCategory : parentCategory.getSubCategories())
                {
                    if(subCategory.isSelected())
                        selectedSubCategories.add(subCategory);
                }
            }
        }
        Random rand = new Random();
        return selectedSubCategories.get(rand.nextInt(selectedSubCategories.size())).getUrlEnd();
    }

    public static void selectAll()
    {
        for(ParentCategory parentCategory : categories)
            parentCategory.selectAll();
    }

    public static void validSelection()
    {
        for(ParentCategory parentCategory : categories)
            if(parentCategory.isSelected())
                return;

        selectAll();
    }

    public static void selectFromString(String input)
    {
        String[] splited = input.split("\\s+");
        for(int i = 0; i < splited.length; i++)
        {
            String[] subsplit = splited[i].split(":");
            categories.get(Integer.parseInt(subsplit[0])).toggleSubCategory(Integer.parseInt(subsplit[1]));
        }
    }

    public static String saveToString()
    {
        String selectedCategories = "";

        for(int i = 0; i < categories.size(); i++)
        {
            if(categories.get(i).isSelected())
            {
                for(int sub = 0; sub < categories.get(i).getSubCategories().size(); sub++)
                {
                    if(categories.get(i).getSubCategories().get(sub).isSelected())
                        selectedCategories += i + ":" + sub + " ";
                }
            }
        }

        return selectedCategories;
    }

    public static void initiateCategories()
    {
        categories.add(new ParentCategory("Animais", new ArrayList<SubCategory>() {{
            add(new SubCategory("Animais", new String[]
                    {
                            "animais/cavalos",
                            "animais/peixes",
                            "animais/aves",
                            "animais/gatos",
                            "animais/animais-de-quinta",
                            "animais/caes",
                            "animais/roedores"
                    }, new Double[]
                    {
                            0.05d,
                            0.07d,
                            0.41d,
                            0.06d,
                            0.09d,
                            0.27d,
                            0.06d
                    }));
            add(new SubCategory("Acessórios", "animais/acessorios-animais"));
        }}, R.drawable.animals));
        categories.add(new ParentCategory("Bebé e Criança", new ArrayList<SubCategory>() {{
            add(new SubCategory("Roupa", "bebes-criancas/roupinhas"));
            add(new SubCategory("Calçado", "bebes-criancas/calcado-bebes"));
        }}, R.drawable.baby));
        categories.add(new ParentCategory("Carros, Motas e Barcos", new ArrayList<SubCategory>() {{
            add(new SubCategory("Carros", "carros-motos-e-barcos/carros"));
            add(new SubCategory("Peças", "carros-motos-e-barcos/pecas-e-acessorios-carros"));
            add(new SubCategory("Outros Veículos", new String[]
                    {
                            "carros-motos-e-barcos/motociclos-scooters",
                            "carros-motos-e-barcos/camioes-veiculos-comerciais",
                            "carros-motos-e-barcos/barcos-lanchas",
                            "carros-motos-e-barcos/autocaravanas-roulotes-reboques",
                            "carros-motos-e-barcos/outros-veiculos",
                            "carros-motos-e-barcos/salvados"
                    }, new Double[]
                    {
                            0.34d,
                            0.41d,
                            0.04d,
                            0.10d,
                            0.07d,
                            0.03d
                    }));
        }}, R.drawable.cars));
        categories.add(new ParentCategory("Desporto", new ArrayList<SubCategory>() {{
            add(new SubCategory("Artigos Desportivos", "desporto-e-lazer/artigos-desportivos"));
            add(new SubCategory("Bicicletas", "desporto-e-lazer/bicicletas"));
        }}, R.drawable.sports));
        categories.add(new ParentCategory("Imóveis", new ArrayList<SubCategory>() {{
            add(new SubCategory("Apartamentos", "imoveis/apartamento-casa-a-venda"));
            add(new SubCategory("Moradias", "imoveis/casas-moradias-para-arrendar-vender"));
            add(new SubCategory("Escritórios e Lojas", "imoveis/escritorios-lojas"));
            add(new SubCategory("Terrenos", "imoveis/terrenos-quintas"));
            add(new SubCategory("Outros Imóveis", new String[]
                    {
                            "imoveis/quartos-para-aluguer",
                            "imoveis/casas-de-ferias",
                            "imoveis/permutas",
                            "imoveis/garagens-estacionamento",
                            "imoveis/estabelecimentos-comerciais-para-alugar-vender"
                    }, new Double[]
                    {
                            0.15d,
                            0.18d,
                            0.13d,
                            0.13d,
                            0.41d
                    }));
        }}, R.drawable.houses));
        categories.add(new ParentCategory("Lazer", new ArrayList<SubCategory>() {{
            add(new SubCategory("Jogos e Brinquedos", "lazer/jogos-brinquedos"));
            add(new SubCategory("Instrumentos Musicais", "lazer/instrumentos-musicais"));
            add(new SubCategory("Livros e Revistas", "lazer/livros-revistas"));
            add(new SubCategory("Antiguidades", "lazer/coleccoes-antiguidades"));
            add(new SubCategory("DVDs e Filmes", "lazer/dvd-filmes"));
            add(new SubCategory("Discos de Vinil, CDs e Música", "lazer/discos-vinil-cds-musica"));
        }}, R.drawable.leisure));
        categories.add(new ParentCategory("Moda", new ArrayList<SubCategory>() {{
            add(new SubCategory("Roupa", "moda/roupa-moda"));
            add(new SubCategory("Calçado", "moda/calcado"));
            add(new SubCategory("Joias, Bijuteria e Relógios", "moda/joias-bijuteria-relogios"));
            add(new SubCategory("Malas e Acessórios", "moda/malas-e-acessorios"));
            add(new SubCategory("Saúde e Beleza", "moda/saude-beleza"));
        }}, R.drawable.fashion));
        categories.add(new ParentCategory("Móveis, Casa e Jardim", new ArrayList<SubCategory>() {{
            add(new SubCategory("Utilidades e Decoração", "moveis-casa-e-jardim/utilidades-e-decoracao"));
            add(new SubCategory("Electrodomésticos", "moveis-casa-e-jardim/electrodomesticos"));
            add(new SubCategory("Jardim e Bricolage", "moveis-casa-e-jardim/jardim-e-bricolage"));
            add(new SubCategory("Móveis e Decoração", "moveis-casa-e-jardim/moveis-decoracao"));
        }}, R.drawable.furniture));
        categories.add(new ParentCategory("Tecnologia e Informática", new ArrayList<SubCategory>() {{
            add(new SubCategory("Videojogos e Consolas", "tecnologia-e-informatica/videojogos-consolas"));
            add(new SubCategory("Computadores e Informática", "tecnologia-e-informatica/computadores-informatica"));
            add(new SubCategory("Electrónica", "tecnologia-e-informatica/electronica"));
            add(new SubCategory("Fotografia, TV e Som", "tecnologia-e-informatica/fotografia-tv-som"));
        }}, R.drawable.technology));
        categories.add(new ParentCategory("Telemoveis e Tablets", new ArrayList<SubCategory>() {{
            add(new SubCategory("Acessórios", "telemoveis-e-tablets/acessorios-telemoveis-tablets"));
            add(new SubCategory("Telemoveis e Tablets", new String[]
                    {
                            "telemoveis-e-tablets/telemoveis",
                            "telemoveis-e-tablets/tablets"
                    }, new Double[]
                    {
                            0.9d,
                            0.1d
                    }));
        }}, R.drawable.phones));
        categories.add(new ParentCategory("Outras Vendas", new ArrayList<SubCategory>() {{
            add(new SubCategory("Outras Vendas", "compra-venda/o-resto"));
        }}, R.drawable.others));
    }

    public static ArrayList<ParentCategory> getCategories()
    {
        return categories;
    }

    public static void setCategories(ArrayList<ParentCategory> categories)
    {
        CategoryHandler.categories = categories;
    }
}
