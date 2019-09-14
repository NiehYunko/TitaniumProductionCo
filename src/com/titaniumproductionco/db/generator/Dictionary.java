package com.titaniumproductionco.db.generator;

import java.util.Random;

public class Dictionary {
    private static String[] ROADS = { "Marinaland", "Carrington Strand", "Lodge Acre", "Oulton Crescent", "Goldfinch Woodlands", "Brackerne Close", "Broom Wood", "Halton Field", "Barnes Esplanade",
            "Harwood Glen", "Naseby Mews", "Lawson Rise", "Phoenix Glade", "Bradford Lanes", "Roxburgh Lawns", "Fort Drove", "Barrack Oaks", "Mitchell Trees", "Webb Mill", "Villa Path",
            "Longlands North", "Wren Cloisters", "Horsley Nook", "Quarry Bank", "Charlock Grove", "Dark Gate", "Hollies Pleasant", "Priors Mill", "White Hart Covert", "Dene Field", "Thorn Covert",
            "Arnside Promenade", "Foston Grove", "Farm Fairway", "White House East", "Woodend Gait", "Crosby Boulevard", "Coopers Town", "Ruskin View", "St Marks Promenade", "Intake Lawns",
            "Fox Orchard", "Orwell Ridge", "Aylesbury Valley", "De Braose Close", "Boston Vale", "Ruskin Bridge", "Burrows Spur", "Elder View", "Harewood Mount", "Fairlea Avenue", "Waterloo Rowans",
            "Beaumont Edge", "Joy Avenue", "Bury Willows", "Elton Heights", "Patterdale Hills", "Stannary Road", "Percival Drove", "Kingsmead Gait", "Mametz Grove", "Martinet Close",
            "Cherrywood Causeway", "Lodge Village", "Malton Oak", "Valentine Crescent", "Rochester Promenade", "Upper Woodcote Village", "Woodstock Mews", "Cowslip Glebe", "Northfield Woods",
            "Aston Moorings", "Dipton Close", "Hutton Copse", "Woodgate Paddocks", "Haydock Glas", "South View Dene", "Washbrook Road", "Chartwell Crest", "Mason Oval", "Whalley Piece",
            "Pershore Avenue", "Andrews Grove", "Abbotts Market", "Ellison Point", "Grove Wood Close", "Hazelwood Green", "Carloggas Grove", "Huxley Glade", "Julian West", "Parker Alley", "Teal East",
            "Boston By-Pass", "Roberts Drove", "Melingriffith Close", "Whitefield Edge", "Turner Gardens", "Chaucer Highway", "Long Elms", "Cathedral Link", "Viscount Square", "Tamar Retreat",
            "Minerva Alley", "Canterbury Willows", "Ogley Crescent", "Whittle Manor", "Privet Road", "Kirkland Strand", "Commercial Top", "Blair Parc", "Dovedale Ride", "Thornfield Avenue",
            "Intake Trees", "Abbotts Garth", "Blackwood Hill", "Elmwood Court", "Clough Row", "Beech Acres", "Edale Dale", "Dovedale Crest", "Heritage Chase", "Mitre Pines", "Cromer South",
            "Bacon Place", "Chelsworth Crescent", "Higher Hoopern Lane", "Middleton Mews", "Barker Walk", "Locksgate", "Brewery Holt", "Clifford Head", "Navigation Walk", "Lambourne Mews",
            "Halstead Moorings", "Brangay Lane", "Blackburn Springs", "Melbourne Paddocks", "Glennie Gardens", "Chesterton Mews", "Cricketers Place", "Ridge Villas", "Wype Drove", "Magdalen Parkway",
            "Mason Fairway", "Bell Tower Close", "Bute Acres", "Willow Villas", "Argyle Circus", "King Walk", "Wessex Lawns", "King George Hollow", "Stoops Close", "Linkfield Lane", "Marston Acres",
            "Haddon Grange", "Greyfriars Retreat", "Mountainhall Place", "Sefton Lanes", "School Spur", "Wheatsheaf Causeway", "Robins By-Pass", "Mount Pleasant Square", "Hood Glade", "Thames Cliff",
            "Petit Well Lane", "Holmpark Crescent", "Leigh Lane", "Caxton Place", "Mellor Way", "Rockingham Causeway", "Kirkby Court", "Vernon Approach", "Minerva Mill", "Bathurst Square",
            "Jackey Lane", "Caen Street", "Belmore Lane", "Sherbourne Parade", "Abbotsford Fairway", "Partridge Moorings", "Woodbury Copse", "Delf Street", "Montagu Spur", "Caernarvon Quadrant",
            "Hedgefield Hurn", "Juniper Woods", "Fitzwilliam Gate", "Wheatley Paddocks", "Huxley Fold", "Bela Close", "King Ground", "Hadrian Street", "Worcester Willows", "Ashbourne Leaze",
            "Bector Lane", "Dean Grove", "Welbeck Yard", "Capel Downs", "Aston Alley", "Dover Beck Close", "Buttress Lane", "Keates Way", "Skylark Mews", "Lingfield Brook", "Peter Las", "Graham Farm",
            "Greyfriars Mount", "Rookery Meadow", "Vicar Road", "Eastcote View", "Hathaway Vale", "Newbridge Lawn", "Hinton Boulevard", "St Anne's Wynd", "Morven Lawn", "Hazelwood Side", "Pump Mews",
            "Sun Heights", "Imperial Glade", "Newbridge Coppice", "Cypress Nook", "Fore Lawn", "Trafalgar Copse", "Bevin Grove", "Wick Fields", "Beaufort Green", "Christchurch Down", "Edison Dale",
            "Clyde Orchard", "Milford Path", "Snowley Park", "Barrack Reach", "Stratford Crest", "Sinclair Ridgeway", "Angus Court", "Beckett Nook", "Field Ridgeway", "Midland Row", "Clark Link",
            "Wykeham Bottom", "Lavender Copse", "Harrier Hills", "Beaufort Cliff", "Hoylake Orchards", "Third Road", "Waveney Spinney", "Minnoch Crescent", "Jones Lawns", "Burbidge Grove",
            "Easy View Lane", "Foundry Leaze", "St Michaels Mount", "Mornington Drive", "Arnside Links", "Old Church Lawn", "Baron Beeches", "Thames Mead", "Falcon Buildings", "Cedars Green",
            "Colwyn Cedars", "Rowans Close", "Vulcan Meadows", "Hinton Grange", "Beechwood Manor", "Windermere Glade", "Village Hills", "Navigation End", "Apsley Mount", "Pump Farm", "Henry Warren",
            "Seafield Brambles", "Carlisle Bridge", "Templepan Lane", "Pine Mead", "Worsley Moor", "Roebuck Oaks", "Erskine Pastures", "Rockram Close", "Park Side", "Wye Down", "Picton Hawthorns",
            "Brickfield Leas", "Garrick Wood", "Treverbyn Rise", "Redwing Mount", "Ring Road Farsley", "Holborn Hall", "Charnwood Woods", "King George Courtyard", "Oakdale Newydd", "Monterey Park",
            "Coach Passage", "Palace Ground", "Lavender Mews", "Wellesley Glade", "Brisbane Grange", "Yew Tree Field", "Norwich Parc", "Fitzroy Furlong", "Capel Ride", "Ambrose Green",
            "Parkstone Esplanade", "Leys Ridgeway", "Gill Circus", "Fort Drift", "Centenary Cottages", "Hampstead Downs", "Appletree Corner", "Northern Pines", "Audley Ride", "Richards Bridge",
            "Eastwood Wharf", "Shepherd Walk", "Chapel Wood", "Watery Boulevard", "Kings Moorings", "Brentwood Paddock", "Hatfield Downs", "Elmhurst Crescent", "Bowling Green Parade",
            "Darlington Oaks", "Cherrywood Wood", "Ivanhoe Haven", "Northcote Chase", "Imperial South", "Wolsey Green", "Southgate Rowans", "Burghley Green", "Elderberry Passage", "Adelaide Cliff",
            "Keble Orchard", "Ashworth West", "Winifred Lodge", "Blackwell Alley", "Wallace Wood", "St Nicholas Market", "Spey East", "Queen's Drove", "Partridge Cottages", "Hawkins Circle",
            "Coppice Lanes", "Edale Parc", "Florence Side", "Holme Hollies", "Briarwood Ridings", "County Oval", "Newland Dene", "Silverdale Chase", "Iris Pleasant", "Yarrow Ridge", "Factory Meadow",
            "Broughton Market", "Lilac Pleasant", "Westmorland Spur", "Waterford Nook", "Parklands Hill", "Kendal Woods", "Mill Hill Acres", "Beeston Cross", "Potters Bridge", "Grafton Terrace",
            "Blackwood Holt", "Reed Gardens", "Silver Birch Passage", "Beauchamp Mews", "Bryn Yard", "Ely Oval", "Wilkinson Lea", "Clarke Lawns", "Ashleigh Passage", "Hazelwood Limes", "Regent Wharf",
            "Brooklyn Estate", "Beauchamp Nook", "Bancroft Orchards", "Rowan Alley", "Burley Hill", "Evergreen Quadrant", "Glamis Oval", "Willoughby Pastures", "Wentworth Woodlands",
            "Old Rectory Isaf", "High Broadway", "Ely Common", "Wolsey Side", "Falmouth Hawthorns", "Branksome Circus", "Somerset Spur", "Cambrian Manor", "Coppice Crest", "Radnor Terrace",
            "Green Garden", "Baxter Fairway", "Fairway Esplanade", "Lime South", "Springwood Piece", "Noble Head", "Woodbine Trees", "Blacksmiths Glebe", "Christopher Lawn", "Calder Ridgeway",
            "Leys Hey", "Hartley Boulevard", "White Hart Oak", "Westbrook Glade", "Great Way", "Stoney Terrace", "Selkirk Hey", "Boston Paddocks", "Rowan Brow", "Lysander Down", "Causeway Mount",
            "St Ann's East", "Vaughan Side", "Cyprus Hey", "Cobden Furlong", "Old Vicarage Woodlands", "Woodcote Side", "Burley Bank", "Acer Isaf", "Vaughan Down", "Meadowsweet Nook",
            "Royal Oak Woodlands", "Nicholson Lanes", "Olive Highway", "Elliott Head", "Ashton Approach", "Walpole Ridge", "Chase Grove", "Ripon Copse", "Connaught Leaze", "Clarendon Top",
            "Oakham Estate", "St John Oak", "Avondale Parade", "Old Church Town", "Newark Lawns", "Shaw Ride", "Skylark Loan", "The Old Glen", "Worsley By-Pass", "Sheridan Paddock", "Leopold Ridge",
            "Pool Walk", "Beeches Furlong", "Orwell Woods", "Stratton Springs", "Redcar Brow", "Owen Brambles", "Cromer Firs", "Empress Lawn", "Ellison Glebe", "Hadrian Ridge", "Clement Meadows",
            "Stratton Warren", "Farley Ride", "Peter Avenue", "Empress Chase", "Chiltern Trees", "Apple Highway", "Elmfield Edge", "Stevenson Loke", "Woodfield Dale", "Green Mews", "Leyland Village",
            "Julian Circle", "Saffron Warren", "East Corner", "Courtenay Cloisters", "Sydney Quadrant", "Pemberton Market", "Railway Pastures", "Olive Mount", "Alnwick Orchard", "Lytton Bridge",
            "Blackthorn Point", "Hilltop Side", "Sydney Field", "Chatsworth Newydd", "Stevenson Lea", "Snowdrop Broadway", "Rosedale Ride", "Chase Hollies", "Lyon Edge", "Fir Brambles",
            "Empress Cedars", "St Stephens By-Pass", "Park View Spur", "Barnard Close", "Pheasant Copse", "Barnes Limes", "Hatton Drift", "Millbank Springs", "Manor Farm Las", "Heathfield Moor",
            "Bolton Row", "Owen Village", "Burley Piece", "Henley Drive", "Junction Acres", "Paterson Yard", "St Martins Acre", "Broadlands South", "Monmouth Hall", "St Lukes Glebe", "Sorrel Glade",
            "Ashfield Covert", "Duchess Maltings", "Ferndale Head", "Southern Birches", "Bryony Head", "Thornhill Newydd", "Bath Leys", "Piper Glas", "Leighton Esplanade", "Prince Holt",
            "Snowdon Fairway", "Charles Brambles", "Chalk Mews", "Taunton Gate", "Frobisher Heights", "Lansdowne Loke", "Fox Way", "Abingdon Oaks", "Edinburgh Bank", "Sunnybank Elms",
            "Cunningham Buildings", "Calder Paddock", "Linnet View", "Granville Orchard", "Shackleton Furlong", "Rosebery Links", "Jersey Passage", "Redcar Quadrant", "Hope Firs", "Ann Road",
            "Link Yard", "Birmingham Wynd", "Adelaide Chase", "Hare Cliff", "Buckland Corner", "Admiral Warren", "Vicarage Manor", "Connaught Leys", "Holborn View", "Wheatley Grange",
            "St Margarets Downs", "Wiltshire Crescent", "Robinson Road", "Brickfield Limes", "Piper Garth", "Coltsfoot Fold", "St Annes Hollow", "Forbes Elms", "Phoenix Wynd", "Hollybush Warren",
            "Bell Brow", "Lismore Bridge", "Moorhouse Garth", "Milford Grove", "Surrey Crescent", "Rosewood View", "Wordsworth Hall", "Manse Cross", "Kennet Top", "Alpha Hill", "Rectory Ridgeway",
            "Randolph Spinney", "Kennet Banks", "Langdale Mount", "Alma Head", "Drummond Edge", "Highfield Gait", "Carrington Royd", "Ward Las", "Farriers Woods", "Wallis Dell", "St Lawrence Lanes",
            "Greenland Alley", "Chatsworth By-Pass", "Tudor Copse", "Penny Point", "Causeway Haven", "Avon Nook", "Milford South", "Austen Las", "Newtown Pines", "Eliot Common", "Brecon End",
            "Argyle Square", "Longwood Cloisters", "Fleet Head", "Carleton Dale", "Ashley Glen", "Vaughan Lea", "Austin Crest", "Gleneagles Moor", "Allan Manor", "Cartwright Ridings",
            "Cobden Moorings", " Pool Grove", "Hanbury Limes", "Waverley Fold", "Hunters Elms", "Castleton Ground", "Stratford Maltings", "Well Ground", "Princes Grove", "Woodhouse Chase",
            "Down Croft", "Onslow Drove", "Redcar Villas", "Holloway Head", "Sherborne Cottages", "Imperial Knoll", "Willow Crescent", "Curtis Acre", "Dean Place", "Bury Celyn", "Eden Garth",
            "White Horse Passage", "Dove Ridings", "Crabtree Circus", "Saffron Circus", "Ashleigh Circus", "Woodview Esplanade", "Brunswick South", "Christchurch Green", "Old Forge Rowans",
            "Chequers Bridge", "Devonshire Paddock", "The Oval", "Lynn Poplars", "Thomas Woods", "Badgers Town", "Knoll Field", "Vulcan Lawns", "Bruce Birches", "Haydon Wood", "Whitehill Court",
            "Lily Oaks", "Telford Glen", "Beach Oaks", "Elm Tree Walk", "King's Spinney", "Rosebank Poplars", "Elgar Boulevard", "St Lukes Approach", "Colliers Garden", "Field Dale",
            "Ullswater Fairway", "Stockwell Downs", "Selby Paddocks", "Jersey Quay", "Longford Rowans", "Lynn Rise", "March Side", "Carisbrooke Side", "Frederick Beeches", "Rosebery Manor",
            "Hunt Spur", "Lord Mead", "Buxton Laurels", "Harding Way", "Golden Passage", "Peacock Ride", "St Stephen's Hall", "Barley Hawthorns", "St Helen's Spinney", "St Ann's Hawthorns",
            "St Andrew's Avenue", "Hardy Mead", "St James Spur", "Stafford Pastures", "Wick Leas", "Leyburn North", "Rectory Beeches", "Bradford End", "Albemarle Dene", "Cornwallis Buildings",
            "Hardy Moor", "Bodmin Park", "Springbank Acre", "Beck Furlong", "Lindisfarne Pastures", "Lindsey Ridings", "Wakefield Strand", "Arlington Laurels", "Blacksmith Hey", "Weston Oval",
            "Stirling Mill", "Gregory Rise", "Dukes Wood", "Canal Lea", "Haven Ride", "Festival Piece", "Riley Loan", "Oban Piece", "Winifred Way", "Lorne Isaf", "Pump Walk", "Huntingdon Bank",
            "Harris Moorings", "Cow Oaks", "Hampshire Lane", "Coopers Grove", "Dunbar Estate", "Siskin Brow", "Morley Market", "Oakham Esplanade", "Wayside Promenade", "Baron Loke", "Airedale Firs",
            "Newport Oak", "Ashley Woods", "St Stephen's By-Pass", "Anvil Banks", "Marlowe Hey", "Appleton Orchards", "Gower Link", "Burleigh Loke", "Blair Cross", "Old Meadow", "Crofton Celyn",
            "Baldwin Close", "Upland Strand", "High Passage", "Dorset Paddock", "Gibson Cliff", "Hartington Brae", "Fleetwood Way", "Hythe Oval", "Weir Close", "Paget Village", "Wessex Villas",
            "Manse Paddocks", "Liverpool Avenue", "Westgate Ridge", "Iris Meadows", "Knowles End", "Eagle Lane", "St Pauls Croft", "Meadows Orchard", "Brisbane Sidings", "Mount Trees", "Harrow Close",
            "Nursery Parc", "Norman Pastures", "Miles Hollies", "Copper Beech Poplars", "Pit Willows", "Park View Hills", "Walpole Common", "Carnegie Gardens", "Martin Farm", "Hillview Brae",
            "Pinewood Manor", "Woodbridge Ridings", "Bevan Meadows", "Roebuck Highway", "Barry Moor", "Newton Buildings", "Arthur Bridge", "Epsom Cliff", "Nairn Crescent", "Harrow Pines",
            "Brentwood Strand", "Ivy Willows", "Woodstock Spinney", "Station Paddocks", "Clover Point", "Brookside Leaze", "Huxley Hollies", "Webster Haven", "Netherfield Avenue", "Parker Alley",
            "Acacia Promenade", "Beechwood Courtyard", "Bellevue Retreat", "Pit South", "Jesmond Acres", "Duncombe Leys", "Walpole Sidings", "Ferry Gate", "Woodhouse Bridge", "Pearson Woods",
            "Dale Elms", "Blackthorn Loke", "Oakley Chase", "Nairn Glas", "Newtown Sidings", "Forest Head", "Grasmere Holt", "St Michaels By-Pass", "Blackberry Barton", "Moss Cross", "Norwood Square",
            "Manse Drift", "Pine Tree Celyn", "Kingsway Meadows", "Lambourne Glen", "Larkspur Loke", "Saunders Head", "Cherwell Oak", "Fir Isaf", "Gardner Mount", "Colville Ridge", "Millbank View",
            "Paxton Isaf", "Railway Causeway", "Wolsey Mount", "Springwood Knoll", "Loxley Spinney", "Lumley Way", "Epping Royd", "Albany Circus", "Sixth Warren", "Hogarth Celyn",
            "St Stephen's Pleasant", "Glenfield Leys", "Burghley Wharf", "Beaumont Piece", "Elgin Rise", "Royal Willows", "Holbrook Mead", "Cyprus Parkway", "Hollow Newydd", "Horsley Parc",
            "Poole Side", "Daisy Hills", "Knox Ride", "Mason Dale", "York Lanes", "Quarry Copse", "Coventry Fairway", "Poole Mead", "Kennedy Cross", "Primrose Fairway", "Allington Head",
            "Higham Glebe", "St Ann's Brow", "Kirby Glebe", "Crossley Avenue", "Black Beeches", "Wiltshire Hills", "Burford Approach", "Hillary Knoll", "Hollin Pines", "Lark Chase", "Warner Wharf",
            "Low Links", "Village Holt", "Glenfield Common", "Victoria Highway", "Broomfield Drove", "Willow Tree Gait", "Oban Pines", "Beatty Cloisters", "Broad Oval", "Ashgrove Heath",
            "Harrington Beeches", "Paradise Row", "Lulworth Elms", "Quarry Grove", "Melbourne Garth", "Weavers Market", "Wilton Strand", "Bower Road", "Woodfield Covert", "Juniper Grove",
            "Convent Hollies", "Hudson Dene", "Pasture Glen", "Lock Meadow", "Bracken Glen", "Christchurch View", "Gower Acre", "Fernhill Path", "Eastbourne Cloisters", "Carter Park", "Fifth Springs",
            "Old School Isaf", "Coombe Woods", "Cecil Link", "Coleridge Fields", "Sefton Bank", "Hilton Poplars", "Springwood Farm", "Horsley Lanes", "Dickens Wynd", "Meadow Quadrant",
            "Hampshire Courtyard", "Fernhill Leys", "Brooks Isaf", "Parkwood Gardens", "Ann Paddocks", "Ripon Paddock", "Moorside Oak", "Woodfield Farm", "Brooklands Vale" };

    private static String[] CITIES = { "Adams Center", "Greenleaf", "Port Richey", "Smith Corner", "Toxey", "Griffithville", "Rosman", "Olympia Fields", "Fleming", "Cashion", "Newport East",
            "New Market", "Irvington", "Garfield Heights", "Craigsville", "Pawcatuck", "Narciso Pena", "Pierce", "Top-of-the-World", "Elk Garden", "Marsing", "Latta", "Deenwood", "Pana",
            "Upper Brookville", "Moroni", "Garcon Point", "Emory", "Supai", "Greenport", "Inglewood", "Oakesdale", "Bishop Hills", "Big Creek", "Ezel", "Birney", "Beersheba Springs", "Macksville",
            "Spring Hope", "Days Creek", "Sugarland Run", "Henniker", "Goodell", "Pleasant Gap", "Pajaro", "Trussville", "Marathon City", "Meadowview Estates", "Coffman Cove", "Solway", "Annex",
            "Star Valley", "Waukegan", "Greilickville", "Nespelem", "Carolina Shores", "Ravanna", "Fossil", "Muscoy", "Salvo", "Hat Island", "Inchelium", "Dannebrog", "Woodland Hills", "Hordville",
            "Rosebush", "Meshoppen", "Greenacres", "Milligan", "Lexa", "Harbor Isle", "Wales", "Golovin", "Pocono Pines", "Shakopee", "New Bavaria", "Havre North", "Beaverville", "Gallup", "Brodhead",
            "Carnelian Bay", "Mulvane", "McCord", "Perryville", "La Conner", "Lashmeet", "Moraine", "O'Neill", "Bonnetsville", "Ashley Heights", "Zwolle", "South Windham", "Reform", "Hale",
            "La Paloma Ranchettes", "Makena", "Joppa", "Rossford", "Stringtown", "North", "Mount Jewett", "Salinas", "Ranchette Estates", "Oldenburg", "Duluth", "Boyds", "Kiefer",
            "White House Station", "Ridgeville", "Kiel", "New Tripoli", "Roundup", "Apache Junction", "Websters Crossing", "Kernville", "Diamondhead Lake", "Atlantic Highlands", "South Hill", "Nixa",
            "West Ocean City", "Los Angeles", "Mayersville", "Brookline", "Middle River", "Iota", "Merrillville", "Port Lions", "Stronach", "Coral Hills", "Mount Pleasant", "Pottawattamie Park",
            "Ellis", "Fort Seneca", "Ballard", "Wray", "Cando", "Castorland", "Grovetown", "Saratoga Springs", "Humacao", "Trumann", "Aledo", "Marble City", "Yaak", "Shongaloo", "Magnolia",
            "Youngwood", "Parkin", "Maringouin", "Glen Raven" };

    private static String[] COUNTRIES = { "Gernori Republic", "Casto Nitedherbya", "Nyezamslands", "Outsaintncipe", "Puerra", "Laosi Islands", "Lua Slandsmico", "Statesginu", "Bourgdaking", "Dengo",
            "Statesstates Laeaststan", "North Caslands", "Republic of Repuhraincardesh", "Cuazbe Andi", "Ami", "Lomve", "Stanveslandskong", "Ubatswama", "Saintko Chimarkku", "Moanguil", "Theyeriaki",
            "Ofqua Andco", "Sricausgiumpu", "Ngastan Ntipierresia", "Bisland", "Ncentneaco", "Zeare Britishntena", "Cairnli", "Lingo Conua", "Mascan Staco", "Slandsdostarosqa", "Boi Pufu",
            "Namayen Jeto", "Chibo Slandbangbia", "Basouth", "Ruthe", "Tviagange", "Southmayotteland", "South Biathio Ranslandgui", "Amerkeyri", "Todor Supadi", "Maal Slandscia", "Lalieba", "Agas",
            "Zija Brunorth", "Landvirandbe", "Tigi Chadzim", "Ofra Lands", "Daphiia", "Tade Giarkmeia", "Lyre Buloupeko", "Milani", "Mauto", "Ofnari", "Slandso", "Machellestu", "Isa Zapit", "Jiu",
            "Puslo Nadomlip", "Eastern Hongi Sopu", "Rialzstan", "Nini Queginlis", "Southmau Arland", "Niablic Lands", "Norto", "Bonba", "Northern Runti Braha", "Roonbiador", "Neapuanew",
            "Mizer Mohonpines", "Costa Lklandlitan", "Kimo", "Ahaistan", "Lonand", "Rixi", "Narates Ngavia", "Cameni Nairera", "Bongothai", "Therslands Andtu", "Blicko Reugialands", "Canof Rusi",
            "Era Dapein", "Zamnorandbarleone", "Trisniania Territories", "Republic of Manshall", "Guernkrainedadblicpri", "Rabgua Rilala", "Rusma", "Engcra Ngocooksle", "Cone Blicbeda", "Reali",
            "Sikia", "Ofru Manlemaar", "Risoublicsogo", "Rdansa Ii", "Poimar", "Keelingi Didian", "Quecro Omibo", "Atishgua", "Nianepa" };

    private static String[] PEOPLE = { "Becker", "Goodman", "Owens", "Meyer", "Whitehouse", "Little", "Casey", "Reeves", "Huang", "Hanson", "Reynolds", "Armstrong", "Fletcher", "Bennett", "Chen",
            "Byrd", "Robbins", "Green", "Hammond", "Solis", "Swanson", "Cruz", "Bauer", "Rodgers", "Chambers", "Anderson", "Dean", "Chandler", "Castro", "Molina", "Frank", "Mendez", "Cooper", "Terry",
            "Wallace", "Johnston", "Fleming", "Craig", "Robinson", "Brown", "Paul", "Wood", "Ryan", "Schmidt", "Lloyd", "Gonzalez", "Edwards", "Miranda", "Shepherd", "Patterson", "Ramirez", "Sims",
            "Pena", "Griffiths", "Carroll", "Murray", "Bates", "Goodwin", "Lawrence", "Conner", "Lin", "Mills", "Clarke", "Taylor", "Curry", "Haynes", "Klein", "Buchanan", "Park", "Berry", "Vazquez",
            "Clayton", "Hale", "Rivas", "Willis", "Allen", "Kelley", "Burton", "Torres", "Harper", "Hamilton", "Bush", "Singh", "Robertson", "Carter", "Ferguson", "Nguyen", "Ball", "Mejia", "Duran",
            "Perry", "Huff", "Warner", "Baldwin", "Young", "Elliott", "Holland", "Anderson", "Barton", "Norris", "Johnson", "Newman", "Griffiths", "Dawson", "Hart", "Davidson", "Hawkins", "Nash",
            "Doyle", "Vargas", "Goodman", "Maldonado", "Wong", "Stephens", "Woods", "Houston", "Mendez", "Armstrong", "Rose", "Yang", "Cole", "Harper", "Owens", "Burgess", "Webb", "Nelson", "Walton",
            "Roman", "Lloyd", "Drake", "Hardy", "Watson", "Alexander", "Camacho", "Bell", "Patton", "Burton", "Smart", "Love", "Hansen", "Poole", "Howell", "Parsons", "Reed", "Martinez", "Moran",
            "Becker", "Day", "Fowler", "Rice", "Baldwin", "Barnes", "James", "Mack", "Coleman", "Connolly", "Mcguire", "Sutton", "Patterson", "Rees", "Cunningham", "Stokes", "Simpson", "Klein",
            "Collins", "Schneider", "Velasquez", "Blair", "Marshall", "Reyes", "Harmon", "Russell", "Santiago", "Mckinney", "Smith", "Carson", "Powell", "Gibbons", "Clark", "Davis", "Mcdonald",
            "Rodriguez", "Meyer", "Gutierrez", "Akhtar", "Mohamed", "Lopez", "Henderson", "Cross", "Hernandez", "Kelly", "Vega", "Castro", "Lucas" };

    public static String randomStreetNumber(Random rand) {
        StringBuilder build = new StringBuilder();
        build.append(rand.nextInt(9) + 1);
        int l = rand.nextInt(3);
        for (int i = 0; i < l + 2; i++) {
            build.append(rand.nextInt(10));
        }
        return build.toString();
    }

    public static String randomState(Random rand) {
        return (char) ('A' + rand.nextInt(26)) + "" + (char) ('A' + rand.nextInt(26));
    }

    public static String randomZip(Random rand) {
        return String.valueOf(rand.nextInt(90000) + 10000);
    }

    public static String randomRoad(Random rand) {
        return randomString(ROADS, rand);
    }

    public static String randomCity(Random rand) {
        return randomString(CITIES, rand);
    }

    public static String randomCountry(Random rand) {
        return randomString(COUNTRIES, rand);
    }

    public static String randomPerson(Random rand) {
        return randomString(PEOPLE, rand) + String.valueOf(personID++);
    }

    private static int personID = 10;

    private static String randomString(String[] arr, Random rand) {
        return arr[rand.nextInt(arr.length)];
    }

}
