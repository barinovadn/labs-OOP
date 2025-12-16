import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.prod.js';
import { Api } from './api.js';
import { ChartManager } from './chart.js';
import { createInitialState, mathEvaluators, mathFunctions } from './state.js';

const MIN_LOADING_TIME = 750;

// Новогодний режим - параметры снега
const SNOWFLAKE_URL = 'https://media.tenor.com/KZnE4lQ6Z2cAAAAi/snowflake.gif';

// Снежинки на фоне
const SNOW_BG_SIZE_MIN = 100;
const SNOW_BG_SIZE_MAX = 300;
const SNOW_BG_SPEED_MIN = 1/10;
const SNOW_BG_SPEED_MAX = 1/5;
const SNOW_BG_COUNT = 14;
const SNOW_BG_Z_INDEX = -2;
const SNOW_BG_OPACITY = 0.4;

// Снежинки спереди
const SNOW_FG_SIZE_MIN = 50;
const SNOW_FG_SIZE_MAX = 100;
const SNOW_FG_SPEED_MIN = 1/5;
const SNOW_FG_SPEED_MAX = 1/2;
const SNOW_FG_COUNT = 6;
const SNOW_FG_Z_INDEX = 9999;
const SNOW_FG_OPACITY = 0.3;

// Рождественские огоньки
const CHRISTMAS_LIGHTS_Z_INDEX = -1;
const CHRISTMAS_LIGHTS_OPACITY = 0.1;
const CHRISTMAS_LIGHTS_SIZE = 1700;
const CHRISTMAS_LIGHTS_ANIMATION_DURATION = 12000;

// Лента
const TICKER_SPEED = 8;
const TICKER_SPEED_HOVER = 2.5;

// Елочка
const CHRISTMAS_TREE_IMAGE = 'https://media.tenor.com/5YU_WXg2X1kAAAAi/christmas-star.gif';
const CHRISTMAS_TREE_SIZE = 250;
const CHRISTMAS_TREE_OFFSET_RIGHT = 25;
const CHRISTMAS_TREE_Z_INDEX = 9998;

const ARTICLES = {
  ru: [
    { title: 'ИСТОРИЯ МАТЕМАТИКА БУРБАКИ ЕГО НЕ СУЩЕСТВОВАЛО А НАУЧНЫЕ РАБОТЫ ПИСАЛО ТАЙНОЕ ОБЩЕСТВО', url: 'https://mathcenter.kpfu.ru/tpost/rd75tr0oj1-istoriya-matematika-burbaki-ego-ne-susch' },
    { title: 'АНДРЕЙ ОКУНЬКОВ О КРАСОТЕ ЗАДАЧ И РОЛИ МАТЕМАТИКИ В ТЕХНОЛОГИЯХ', url: 'https://mathcenter.kpfu.ru/tpost/dbf63drsc1-andrei-okunkov-o-krasote-zadach-i-roli-m' },
    { title: 'ОЛИМПИАДА ZAMACODE ОБЪЕДИНИЛА ШКОЛЬНИКОВ ОТ БОГАТЫХ САБОВ ДО ТАНЗАНИИ', url: 'https://mathcenter.kpfu.ru/tpost/dbx1tnhzi1-olimpiada-zamacode-obedinila-shkolnikov' },
    { title: 'ДЕНЬ ТЕОРЕМЫ ПИФАГОРА', url: 'https://mathcenter.kpfu.ru/tpost/cak6psd2g1-den-teoremi-pifagora' },
    { title: 'ПОТАЙНОЙ ВХОД КТО И КАК ПЕРВЫМ ДОДУМАЛСЯ ДО ПОПУЛЯРНОГО АЛГОРИТМА ШИФРОВАНИЯ RSA', url: 'https://mathcenter.kpfu.ru/tpost/8sjp8ekaj1-potainoi-vhod-kto-i-kak-pervim-dodumalsy' },
    { title: 'ОТКРЫТ НОВЫЙ СПОСОБ НАХОЖДЕНИЯ ПРОСТЫХ ЧИСЕЛ', url: 'https://mathcenter.kpfu.ru/tpost/zonh2br7v1-otkrit-novii-sposob-nahozhdeniya-prostih' },
    { title: 'АКАДЕМИК РОБЕРТ НИГМАТУЛИН ПРАЗДНУЕТ ЮБИЛЕЙ', url: 'https://mathcenter.kpfu.ru/tpost/79ioe0obr1-akademik-robert-nigmatulin-prazdnuet-yub' },
    { title: 'МАТЕМАТИКИ МГУ ПРЕДЛОЖИЛИ НОВЫЙ МЕТОД РАБОТЫ С ДАННЫМИ ВЫСОКОЙ РАЗМЕРНОСТИ', url: 'https://mathcenter.kpfu.ru/tpost/c26bb1mag1-matematiki-mgu-predlozhili-novii-metod-r' },
    { title: 'В КАЗАНСКОМ УНИВЕРСИТЕТЕ ПОЧТИЛИ ПАМЯТЬ МАТЕМАТИКА ВЛАДИМИРА ФРИДЛЕНДЕРА', url: 'https://mathcenter.kpfu.ru/tpost/2j75c21n51-v-kazanskom-universitete-pochtili-pamyat' },
    { title: 'МАТЕМАТИКИ ОПИСАЛИ АЛГОРИТМ ВЗЛОМА ДЛЯ КВАНТОВЫХ СИСТЕМ ШИФРОВАНИЯ', url: 'https://mathcenter.kpfu.ru/tpost/ylph3znrb1-matematiki-opisali-algoritm-vzloma-dlya' },
    { title: 'МАТЕМАТИК ИЗ ФИНЛЯНДИИ РЕШИЛА ЗАДАЧУ КОТОРАЯ ОСТАВАЛАСЬ БЕЗ ОТВЕТА 44 ГОДА', url: 'https://mathcenter.kpfu.ru/tpost/23y59ovmd1-matematik-iz-finlyandii-reshila-zadachu' },
    { title: 'МАТЕМАТИЧЕСКАЯ КУЛЬТУРА ОБЩЕСТВА ЕЁ ЗНАЧЕНИЕ И РАЗВИТИЕ', url: 'https://mathcenter.kpfu.ru/tpost/bd6ogahoh1-matematicheskaya-kultura-obschestva-eyo' }
  ],
  en: [
    { title: 'THE HISTORY OF MATHEMATICIAN BOURBAKI HE NEVER EXISTED AND A SECRET SOCIETY WROTE SCIENTIFIC PAPERS', url: 'https://mathcenter.kpfu.ru/tpost/rd75tr0oj1-istoriya-matematika-burbaki-ego-ne-susch' },
    { title: 'ANDREI OKOUNKOV ON THE BEAUTY OF PROBLEMS AND THE ROLE OF MATHEMATICS IN TECHNOLOGY', url: 'https://mathcenter.kpfu.ru/tpost/dbf63drsc1-andrei-okunkov-o-krasote-zadach-i-roli-m' },
    { title: 'ZAMACODE OLYMPIAD UNITED SCHOOLCHILDREN FROM RICH SABBS TO TANZANIA', url: 'https://mathcenter.kpfu.ru/tpost/dbx1tnhzi1-olimpiada-zamacode-obedinila-shkolnikov' },
    { title: 'PYTHAGOREAN THEOREM DAY', url: 'https://mathcenter.kpfu.ru/tpost/cak6psd2g1-den-teoremi-pifagora' },
    { title: 'SECRET ENTRANCE WHO AND HOW FIRST CAME UP WITH THE POPULAR RSA ENCRYPTION ALGORITHM', url: 'https://mathcenter.kpfu.ru/tpost/8sjp8ekaj1-potainoi-vhod-kto-i-kak-pervim-dodumalsy' },
    { title: 'NEW METHOD FOR FINDING PRIME NUMBERS DISCOVERED', url: 'https://mathcenter.kpfu.ru/tpost/zonh2br7v1-otkrit-novii-sposob-nahozhdeniya-prostih' },
    { title: 'ACADEMICIAN ROBERT NIGMATULIN CELEBRATES ANNIVERSARY', url: 'https://mathcenter.kpfu.ru/tpost/79ioe0obr1-akademik-robert-nigmatulin-prazdnuet-yub' },
    { title: 'MOSCOW STATE UNIVERSITY MATHEMATICIANS PROPOSE NEW METHOD FOR WORKING WITH HIGH DIMENSIONAL DATA', url: 'https://mathcenter.kpfu.ru/tpost/c26bb1mag1-matematiki-mgu-predlozhili-novii-metod-r' },
    { title: 'KAZAN UNIVERSITY HONORS MEMORY OF MATHEMATICIAN VLADIMIR FRIDLENDER', url: 'https://mathcenter.kpfu.ru/tpost/2j75c21n51-v-kazanskom-universitete-pochtili-pamyat' },
    { title: 'MATHEMATICIANS DESCRIBE HACKING ALGORITHM FOR QUANTUM ENCRYPTION SYSTEMS', url: 'https://mathcenter.kpfu.ru/tpost/ylph3znrb1-matematiki-opisali-algoritm-vzloma-dlya' },
    { title: 'FINNISH MATHEMATICIAN SOLVED PROBLEM THAT REMAINED UNSOLVED FOR 44 YEARS', url: 'https://mathcenter.kpfu.ru/tpost/23y59ovmd1-matematik-iz-finlyandii-reshila-zadachu' },
    { title: 'MATHEMATICAL CULTURE OF SOCIETY ITS SIGNIFICANCE AND DEVELOPMENT', url: 'https://mathcenter.kpfu.ru/tpost/bd6ogahoh1-matematicheskaya-kultura-obschestva-eyo' }
  ]
};

// Цвета для рождественских огоньков
const CHRISTMAS_COLORS = [
  'rgba(255, 0, 0, 1)',
  'rgba(0, 255, 0, 1)',
  'rgba(255, 255, 0, 1)',
  'rgba(255, 0, 255, 1)',
  'rgba(0, 255, 255, 1)',
  'rgba(255, 165, 0, 1)',
];

const LOADING_TEXTS = {
  en: [
    'Wait...',
    'Loading...',
    'Fetching...',
    'One second...',
    'One moment...',
    'Almost there...',
    'Here it comes...',
    'Any second now...',
    'Just a little longer...'
  ],
  ru: [
    'Ждем...',
    'Почти...',
    'Момент...',
    'Вот-вот...',
    'Секунду...',
    'Уже почти...',
    'Уже вот-вот...',
    'Еще чуть-чуть...',
    'Еще немного...',
    'Еще чуток...',
    'С минуты на минуту...'
  ]
};

const api = new Api('/api');
const chart = new ChartManager('fn-chart');
const STORAGE_KEY = 'labs-oop-ui';
const AD_FALLBACK_URL = 'https://adryd.co/wits.mp4';
const themes = ['dark','light','neo','sunset','ocean','forest','candy','mono','cyber'];
const edgeThemes = ['round','soft','rough'];
const LIMITS = {
  NAME_MAX: 128,
  TYPE_MAX: 16,
  NUMBER_ABS_MAX: 1_000_000, // намного ниже потенциало опасных чисел
  POINTS_MIN: 2,
  POINTS_MAX: 1000,
};
const adInventory = [
  { image: 'https://media1.tenor.com/m/rXvFQlQm87AAAAAC/mc-donalds-big-mac.gif', url: 'https://vkusnoitochka.ru/' },
  { image: 'https://media1.tenor.com/m/khjcfCe2rJQAAAAd/grubhub-grubhub-ad.gif', url: 'https://eda.yandex.ru/moscow?shippingType=delivery' },
  { image: 'https://media1.tenor.com/m/Z50Lj31JIaQAAAAC/fruit-gushers-commercial.gif', url: 'https://shopozz.ru/brand/fruit-gushers' },
  { image: 'https://media1.tenor.com/m/qjkL2zGAr0gAAAAC/gushers-fruit.gif', url: 'https://shopozz.ru/brand/fruit-gushers' },
  { image: 'https://media1.tenor.com/m/b9HC57Nz_ukAAAAC/fruit-gushers-fruit-snacks.gif', url: 'https://shopozz.ru/brand/fruit-gushers' },
  { image: 'https://media1.tenor.com/m/NxUNh0YFzqIAAAAC/lol-bored.gif', url: 'https://shopozz.ru/brand/fruit-gushers' },
  { image: 'https://media1.tenor.com/m/G1bFSidJ2UgAAAAC/fruit-gushers-fruit-snacks.gif', url: 'https://shopozz.ru/brand/fruit-gushers' },
  { image: 'https://media1.tenor.com/m/fEvEbSrVtxQAAAAd/gushers.gif', url: 'https://shopozz.ru/brand/fruit-gushers' },
  { image: 'https://media1.tenor.com/m/wvXUGY98sdsAAAAd/snacks-popcorners.gif', url: 'https://www.youtube.com/watch?v=Pj9a80NOAFA' },
  { image: 'https://media1.tenor.com/m/nug8p803zP8AAAAC/mug-advertisement-downsign.gif' },
  { image: 'https://media1.tenor.com/m/x2zhzsCsWfgAAAAd/animated-gif-banner-animated-banner.gif' },
  { image: 'https://media1.tenor.com/m/RfltIasTb3AAAAAC/saul-goodman.gif', url: 'https://youtu.be/iId5WDsYxZ4' },
  { image: 'https://media1.tenor.com/m/FgGzurd1hEgAAAAd/saul-saul-goodman.gif', url: 'https://youtu.be/iId5WDsYxZ4' },
  { image: 'https://media1.tenor.com/m/ykJ1zn7eYEoAAAAC/whopper-burger-king.gif', url: 'https://www.youtube.com/watch?v=UB-3cfpfOCs&list=PL7qjIFJDIlAxumVbTe3t_l12_69Vd0-PD' },
  { image: 'https://media1.tenor.com/m/0h2mFwKnozgAAAAC/chicken-chicken-chicken-chicken-italian-spicy-bacon-chicken.gif', url: 'https://www.youtube.com/watch?v=UB-3cfpfOCs&list=PL7qjIFJDIlAxumVbTe3t_l12_69Vd0-PD' },
  { image: 'https://media1.tenor.com/m/4jZeCj-nLiUAAAAC/burger-king-stacker.gif', url: 'https://www.youtube.com/watch?v=UB-3cfpfOCs&list=PL7qjIFJDIlAxumVbTe3t_l12_69Vd0-PD' },
  { image: 'https://media1.tenor.com/m/QAbsaaLfGRQAAAAd/megafucker-with-cheese-simplelfips.gif', url: 'https://www.youtube.com/watch?v=UB-3cfpfOCs&list=PL7qjIFJDIlAxumVbTe3t_l12_69Vd0-PD' },
  { image: 'https://media1.tenor.com/m/SFJH90Nts4wAAAAC/mcdonalds-breakfast.gif' },
  { image: 'https://media1.tenor.com/m/S4YGL__spx0AAAAC/mcdonalds-fast-food.gif' },
  { image: 'https://media1.tenor.com/m/n4qKL1TLBHwAAAAC/pepsi-ads.gif' },
  { image: 'https://media1.tenor.com/m/6pQSGAT-l3MAAAAd/us-open-8ball-championship.gif' },
  { image: 'https://ssau.ru/storage/carousel/images/file_68f8eead53bba1.39905816.jpg', url: 'https://ssau.ru/' },
  { image: 'https://ssau.ru/storage/carousel/images/file_68f8eead53bba1.39905816.jpg', url: 'https://ssau.ru/' },
  { image: 'https://ssau.ru/storage/carousel/images/file_68f8eead53bba1.39905816.jpg', url: 'https://ssau.ru/' },
].map((item) => ({
  ...item,
  url: item.url || AD_FALLBACK_URL,
}));

function ensureSafeNumber(raw, label = 'Значение') {
  const num = Number(raw);
  if (!Number.isFinite(num)) throw new Error(`${label}: введите число`);
  if (Math.abs(num) > LIMITS.NUMBER_ABS_MAX) {
    throw new Error(`${label}: |значение| должно быть ≤ ${LIMITS.NUMBER_ABS_MAX}`);
  }
  return num;
}

function ensureOptionalNumber(raw, label) {
  if (raw === null || raw === undefined || raw === '') return null;
  return ensureSafeNumber(raw, label);
}

function ensureName(raw, fallback = 'f(x)') {
  const name = (raw || '').trim();
  if (name && name.length > LIMITS.NAME_MAX) {
    throw new Error(`Имя не длиннее ${LIMITS.NAME_MAX} символов`);
  }
  return name || fallback;
}

function ensureType(raw) {
  const type = (raw || '').trim();
  if (!type) throw new Error('Укажите тип функции (1-16 символов)');
  if (type.length < 1 || type.length > LIMITS.TYPE_MAX) {
    throw new Error(`Тип функции: длина 1-${LIMITS.TYPE_MAX} символов`);
  }
  return type;
}

function ensurePointsCount(raw) {
  const num = Number(raw);
  if (!Number.isInteger(num)) throw new Error('Количество точек должно быть целым числом');
  if (num < LIMITS.POINTS_MIN || num > LIMITS.POINTS_MAX) {
    throw new Error(`Количество точек: от ${LIMITS.POINTS_MIN} до ${LIMITS.POINTS_MAX}`);
  }
  return num;
}

function sanitizePoints(points = []) {
  if (!Array.isArray(points) || points.length < LIMITS.POINTS_MIN) {
    throw new Error(`Нужно минимум ${LIMITS.POINTS_MIN} точки`);
  }
  return points.map((p, idx) => ({
    xValue: ensureSafeNumber(p.xValue ?? p.x, `x[${idx + 1}]`),
    yValue: ensureSafeNumber(p.yValue ?? p.y, `y[${idx + 1}]`),
  }));
}
const logos = [
  'https://media.tenor.com/RnC4v5oEP34AAAAi/bmw-logo.gif',
  'https://media.tenor.com/-GNA5aKsKYwAAAAi/gato-kl-cat.gif',
  'https://media.tenor.com/mVVfN6bQKfEAAAAi/spinning-rat-stupid-rat.gif',
  'https://media.tenor.com/surULijyhSsAAAAj/hi-greetings.gif',
  'https://media.tenor.com/qYSjiwLs2zgAAAAj/fries-spin.gif',
  'https://media.tenor.com/wkorbIbE7yQAAAAi/peaches-neko.gif',
  'https://media.tenor.com/HK9yi0doH1QAAAAi/nigiri-tamago-sushi.gif',
  'https://media.tenor.com/oYw-r_wNvZIAAAAi/duck-flipper.gif',
  'https://media.tenor.com/qJRMLPlR3_8AAAAi/maxwell-cat.gif',
  'https://media.tenor.com/FOeod1coD7wAAAAi/neuro-fumo-spin.gif',
  'https://media.tenor.com/ZRoKjk6MpMsAAAAi/bowling-alley-strike-3d-model.gif',
  'https://media.tenor.com/LROEAFG0GZMAAAAj/smug-jug-3d.gif',
  'https://media.tenor.com/g--QPe6ExSEAAAAj/earth-spin.gif',
  'https://media.tenor.com/mVVfN6bQKfEAAAAj/spinning-rat-stupid-rat.gif',
  'https://media.tenor.com/zHP9BQROq0AAAAAi/doggo-spin.gif',
  'https://media.tenor.com/UiXJKFiyeosAAAAj/dog-gerald.gif',
  'https://media1.tenor.com/m/_rIatT7CqskAAAAd/bird-pigeon.gif',
  'https://media1.tenor.com/m/_rIatT7CqskAAAAd/bird-pigeon.gif',
];
const themeIcons = {
  ocean: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Beer-loop SVG Icon</title><mask id="lineMdBeerLoop0"><path stroke="#fff" stroke-width="2" d="M18 7C16 7 15 9 13 9C11 9 10 7 8 7C6 7 5 9 3 9C1 9 0 7 -2 7C-4 7 -5 9 -7 9" opacity="0"><animateMotion calcMode="linear" dur="3s" path="M0 0h10" repeatCount="indefinite"/><animate fill="freeze" attributeName="opacity" begin="0.6s" dur="0.5s" values="0;1"/></path></mask><path fill="none" stroke="currentColor" stroke-dasharray="60" stroke-dashoffset="60" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M18 3L16 21H7L5 3z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.6s" values="60;0"/></path><path fill="currentColor" d="M18 3L16 21H7L5 3z" mask="url(#lineMdBeerLoop0)"/></svg>`,
  candy: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Cake SVG Icon</title><g fill="none" stroke="currentColor" stroke-width="2"><path stroke-dasharray="28" stroke-dashoffset="28" d="M12 10H18C19.1046 10 20 10.8954 20 12V21H12"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.4s" values="28;56"/></path><path stroke-dasharray="28" stroke-dashoffset="28" d="M12 21H4V12C4 10.8954 4.89543 10 6 10H12"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.4s" values="28;0"/></path><path stroke-dasharray="4" stroke-dashoffset="4" stroke-linecap="round" stroke-linejoin="round" d="M12 10V8" opacity="0"><set attributeName="opacity" begin="0.4s" to="1"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.4s" dur="0.2s" values="4;0"/></path><path stroke-dasharray="20" stroke-dashoffset="20" d="M4 16H5C7 16 8.5 14 8.5 14C8.5 14 10 16 12 16C14 16 15.5 14 15.5 14C15.5 14 17 16 19 16H20" opacity="0"><set attributeName="opacity" begin="0.4s" to="1"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.5s" dur="0.4s" values="20;0"/></path></g><path fill="currentColor" d="M14 4C14 5.10457 13.1046 6 12 6C10.8954 6 10 5.10457 10 4C10 2.89543 12 0 12 0C12 0 14 2.89543 14 4Z" opacity="0"><set attributeName="opacity" begin="0.6s" to="1"/><animate fill="freeze" attributeName="d" begin="0.6s" dur="0.2s" values="M13 5C13 5.5 12.5 6 12 6C11.5 6 11 5.5 11 5C11 4.5 12 4 12 4C12 4 13 4.5 13 5Z;M14 4C14 5.10457 13.1046 6 12 6C10.8954 6 10 5.10457 10 4C10 2.89543 12 0 12 0C12 0 14 2.89543 14 4Z"/></path></svg>`,
  cyber: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Lightbulb SVG Icon</title><path fill="none" stroke="currentColor" stroke-dasharray="46" stroke-dashoffset="46" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 17H9V14.1973C7.2066 13.1599 6 11.2208 6 9C6 5.68629 8.68629 3 12 3C15.3137 3 18 5.68629 18 9C18 11.2208 16.7934 13.1599 15 14.1973V17z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.4s" values="46;0"/></path><rect width="6" height="0" x="9" y="20" fill="currentColor" rx="1"><animate fill="freeze" attributeName="height" begin="0.5s" dur="0.2s" values="0;2"/></rect></svg>`,
  mono: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Coffee-loop SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path stroke-dasharray="48" stroke-dashoffset="48" d="M17 9v9a3 3 0 0 1-3 3H8a3 3 0 0 1-3-3V9z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.6s" values="48;0"/></path><path stroke-dasharray="14" stroke-dashoffset="14" d="M17 14H20C20.55 14 21 13.55 21 13V10C21 9.45 20.55 9 20 9H17"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.6s" dur="0.2s" values="14;28"/></path></g><mask id="lineMdCoffeeLoop0"><path fill="none" stroke="#fff" stroke-width="2" d="M8 0c0 2-2 2-2 4s2 2 2 4-2 2-2 4 2 2 2 4M12 0c0 2-2 2-2 4s2 2 2 4-2 2-2 4 2 2 2 4M16 0c0 2-2 2-2 4s2 2 2 4-2 2-2 4 2 2 2 4"><animateMotion calcMode="linear" dur="3s" path="M0 0v-8" repeatCount="indefinite"/></path></mask><rect width="24" height="0" y="7" fill="currentColor" mask="url(#lineMdCoffeeLoop0)"><animate fill="freeze" attributeName="y" begin="0.8s" dur="0.6s" values="7;2"/><animate fill="freeze" attributeName="height" begin="0.8s" dur="0.6s" values="0;5"/></rect></svg>`,
  sunset: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Cloud-outline-loop SVG Icon</title><mask id="lineMdCloudOutlineLoop0"><g fill="#fff"><circle cx="12" cy="10" r="6"><animate attributeName="cx" dur="30s" repeatCount="indefinite" values="12;11;12;13;12"/></circle><rect width="9" height="8" x="8" y="12"/><rect width="17" height="12" x="1" y="8" rx="6"><animate attributeName="x" dur="21s" repeatCount="indefinite" values="1;0;1;2;1"/></rect><rect width="17" height="10" x="6" y="10" rx="5"><animate attributeName="x" dur="17s" repeatCount="indefinite" values="6;5;6;7;6"/></rect></g><circle cx="12" cy="10" r="4"><animate attributeName="cx" dur="30s" repeatCount="indefinite" values="12;11;12;13;12"/></circle><rect width="8" height="8" x="8" y="10"><animate attributeName="x" dur="30s" repeatCount="indefinite" values="8;7;8;9;8"/></rect><rect width="11" height="8" x="3" y="10" rx="4"><animate attributeName="x" dur="21s" repeatCount="indefinite" values="3;2;3;4;3"/></rect><rect width="13" height="6" x="8" y="12" rx="3"><animate attributeName="x" dur="17s" repeatCount="indefinite" values="8;7;8;9;8"/></rect></mask><rect width="24" height="24" fill="currentColor" mask="url(#lineMdCloudOutlineLoop0)"/></svg>`,
  neo: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Computer SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path stroke-dasharray="6" stroke-dashoffset="6" d="M12 21H17M12 21H7"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.3s" values="6;0"/></path><path stroke-dasharray="6" stroke-dashoffset="6" d="M12 21V17"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.2s" values="6;0"/></path><path stroke-dasharray="64" stroke-dashoffset="64" d="M12 17H3V5H21V17Z"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.3s" dur="0.6s" values="64;0"/></path></g></svg>`,
  light: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Sunny-outline-loop SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2"><path stroke-dasharray="34" stroke-dashoffset="34" d="M12 7C14.76 7 17 9.24 17 12C17 14.76 14.76 17 12 17C9.24 17 7 14.76 7 12C7 9.24 9.24 7 12 7"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.4s" values="34;0"/></path><g stroke-dasharray="2" stroke-dashoffset="2"><path d="M0 0"><animate fill="freeze" attributeName="d" begin="0.5s" dur="0.2s" values="M12 19v1M19 12h1M12 5v-1M5 12h-1;M12 21v1M21 12h1M12 3v-1M3 12h-1"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.5s" dur="0.2s" values="2;0"/></path><path d="M0 0"><animate fill="freeze" attributeName="d" begin="0.7s" dur="0.2s" values="M17 17l0.5 0.5M17 7l0.5 -0.5M7 7l-0.5 -0.5M7 17l-0.5 0.5;M18.5 18.5l0.5 0.5M18.5 5.5l0.5 -0.5M5.5 5.5l-0.5 -0.5M5.5 18.5l-0.5 0.5"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.7s" dur="0.2s" values="2;0"/></path><animateTransform attributeName="transform" dur="30s" repeatCount="indefinite" type="rotate" values="0 12 12;360 12 12"/></g></g></svg>`,
  dark: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Sunny-outline-to-moon-alt-loop-transition SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><g stroke-dasharray="2"><path d="M12 21v1M21 12h1M12 3v-1M3 12h-1"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.2s" values="4;2"/></path><path d="M18.5 18.5l0.5 0.5M18.5 5.5l0.5 -0.5M5.5 5.5l-0.5 -0.5M5.5 18.5l-0.5 0.5"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.2s" dur="0.2s" values="4;2"/></path></g><path d="M7 6 C7 12.08 11.92 17 18 17 C18.53 17 19.05 16.96 19.56 16.89 C17.95 19.36 15.17 21 12 21 C7.03 21 3 16.97 3 12 C3 8.83 4.64 6.05 7.11 4.44 C7.04 4.95 7 5.47 7 6 Z" opacity="0"><set attributeName="opacity" begin="0.5s" to="1"/></path></g><g fill="none" stroke="currentColor" stroke-dasharray="4" stroke-dashoffset="4" stroke-linecap="round" stroke-linejoin="round"><path d="M13 4h1.5M13 4h-1.5M13 4v1.5M13 4v-1.5"><animate id="lineMdSunnyOutlineToMoonAltLoopTransition0" fill="freeze" attributeName="stroke-dashoffset" begin="0.6s;lineMdSunnyOutlineToMoonAltLoopTransition0.begin+6s" dur="0.4s" values="4;0"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="lineMdSunnyOutlineToMoonAltLoopTransition0.begin+2s;lineMdSunnyOutlineToMoonAltLoopTransition0.begin+4s" dur="0.4s" values="4;0"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="lineMdSunnyOutlineToMoonAltLoopTransition0.begin+1.2s;lineMdSunnyOutlineToMoonAltLoopTransition0.begin+3.2s;lineMdSunnyOutlineToMoonAltLoopTransition0.begin+5.2s" dur="0.4s" values="0;4"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition0.begin+1.8s" to="M12 5h1.5M12 5h-1.5M12 5v1.5M12 5v-1.5"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition0.begin+3.8s" to="M12 4h1.5M12 4h-1.5M12 4v1.5M12 4v-1.5"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition0.begin+5.8s" to="M13 4h1.5M13 4h-1.5M13 4v1.5M13 4v-1.5"/></path><path d="M19 11h1.5M19 11h-1.5M19 11v1.5M19 11v-1.5"><animate id="lineMdSunnyOutlineToMoonAltLoopTransition1" fill="freeze" attributeName="stroke-dashoffset" begin="1s;lineMdSunnyOutlineToMoonAltLoopTransition1.begin+6s" dur="0.4s" values="4;0"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="lineMdSunnyOutlineToMoonAltLoopTransition1.begin+2s;lineMdSunnyOutlineToMoonAltLoopTransition1.begin+4s" dur="0.4s" values="4;0"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="lineMdSunnyOutlineToMoonAltLoopTransition1.begin+1.2s;lineMdSunnyOutlineToMoonAltLoopTransition1.begin+3.2s;lineMdSunnyOutlineToMoonAltLoopTransition1.begin+5.2s" dur="0.4s" values="0;4"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition1.begin+1.8s" to="M17 11h1.5M17 11h-1.5M17 11v1.5M17 11v-1.5"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition1.begin+3.8s" to="M18 12h1.5M18 12h-1.5M18 12v1.5M18 12v-1.5"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition1.begin+5.8s" to="M19 11h1.5M19 11h-1.5M19 11v1.5M19 11v-1.5"/></path><path d="M19 4h1.5M19 4h-1.5M19 4v1.5M19 4v-1.5"><animate id="lineMdSunnyOutlineToMoonAltLoopTransition2" fill="freeze" attributeName="stroke-dashoffset" begin="2.8s;lineMdSunnyOutlineToMoonAltLoopTransition2.begin+6s" dur="0.4s" values="4;0"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="lineMdSunnyOutlineToMoonAltLoopTransition2.begin+2s" dur="0.4s" values="4;0"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="lineMdSunnyOutlineToMoonAltLoopTransition2.begin+1.2s;lineMdSunnyOutlineToMoonAltLoopTransition2.begin+3.2s" dur="0.4s" values="0;4"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition2.begin+1.8s" to="M20 5h1.5M20 5h-1.5M20 5v1.5M20 5v-1.5"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition2.begin+5.8s" to="M19 4h1.5M19 4h-1.5M19 4v1.5M19 4v-1.5"/></path></g><mask id="lineMdSunnyOutlineToMoonAltLoopTransition3"><circle cx="12" cy="12" r="12" fill="#fff"/><circle cx="12" cy="12" r="4"><animate fill="freeze" attributeName="r" begin="0.1s" dur="0.4s" values="4;8"/></circle><circle cx="22" cy="2" r="3" fill="#fff"><animate fill="freeze" attributeName="cx" begin="0.1s" dur="0.4s" values="22;18"/><animate fill="freeze" attributeName="cy" begin="0.1s" dur="0.4s" values="2;6"/><animate fill="freeze" attributeName="r" begin="0.1s" dur="0.4s" values="3;12"/></circle><circle cx="22" cy="2" r="1"><animate fill="freeze" attributeName="cx" begin="0.1s" dur="0.4s" values="22;18"/><animate fill="freeze" attributeName="cy" begin="0.1s" dur="0.4s" values="2;6"/><animate fill="freeze" attributeName="r" begin="0.1s" dur="0.4s" values="1;10"/></circle></mask><circle cx="12" cy="12" r="6" fill="currentColor" mask="url(#lineMdSunnyOutlineToMoonAltLoopTransition3)"><set attributeName="opacity" begin="0.5s" to="0"/><animate fill="freeze" attributeName="r" begin="0.1s" dur="0.4s" values="6;10"/></circle></svg>`,
  default: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Paint-drop SVG Icon</title><path fill="none" stroke="currentColor" stroke-dasharray="28" stroke-dashoffset="28" stroke-linecap="round" stroke-width="2" d="M12 3C12 3 19 9 19 15C19 17 18 21 12 21M12 3C12 3 5 9 5 15C5 17 6 21 12 21"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.4s" values="28;0"/></path></svg>`,
};
const edgeIcons = {
  round: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Emoji-smile SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2"><path stroke-dasharray="60" stroke-dashoffset="60" d="M12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="60;0"/></path><path stroke-dasharray="14" stroke-dashoffset="14" d="M8 14C8.5 15.5 9.79086 17 12 17C14.2091 17 15.5 15.5 16 14"><animate fill="freeze" attributeName="stroke-dashoffset" begin="1s" dur="0.2s" values="14;0"/></path></g><g fill="currentColor" fill-opacity="0"><ellipse cx="9" cy="9.5" rx="1" ry="1.5"><animate fill="freeze" attributeName="fill-opacity" begin="0.6s" dur="0.2s" values="0;1"/></ellipse><ellipse cx="15" cy="9.5" rx="1" ry="1.5"><animate fill="freeze" attributeName="fill-opacity" begin="0.8s" dur="0.2s" values="0;1"/></ellipse></g></svg>`,
  soft: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Emoji-neutral SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2"><path stroke-dasharray="60" stroke-dashoffset="60" d="M12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="60;0"/></path><path stroke-dasharray="10" stroke-dashoffset="10" d="M8 15H16"><animate fill="freeze" attributeName="stroke-dashoffset" begin="1s" dur="0.2s" values="10;0"/></path></g><g fill="currentColor" fill-opacity="0"><ellipse cx="9" cy="9.5" rx="1" ry="1.5"><animate fill="freeze" attributeName="fill-opacity" begin="0.6s" dur="0.2s" values="0;1"/></ellipse><ellipse cx="15" cy="9.5" rx="1" ry="1.5"><animate fill="freeze" attributeName="fill-opacity" begin="0.8s" dur="0.2s" values="0;1"/></ellipse></g></svg>`,
  rough: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Emoji-frown SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2"><path stroke-dasharray="60" stroke-dashoffset="60" d="M12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="60;0"/></path><path stroke-dasharray="14" stroke-dashoffset="14" d="M8 16C8.5 15 9.79086 14 12 14C14.2091 14 15.5 15 16 16"><animate fill="freeze" attributeName="stroke-dashoffset" begin="1s" dur="0.2s" values="14;0"/></path></g><g fill="currentColor" fill-opacity="0"><ellipse cx="9" cy="9.5" rx="1" ry="1.5"><animate fill="freeze" attributeName="fill-opacity" begin="0.6s" dur="0.2s" values="0;1"/></ellipse><ellipse cx="15" cy="9.5" rx="1" ry="1.5"><animate fill="freeze" attributeName="fill-opacity" begin="0.8s" dur="0.2s" values="0;1"/></ellipse></g></svg>`,
};
const adCloseIcon = `<svg xmlns="http://www.w3.org/2000/svg" width="384" height="512" viewBox="0 0 384 512"><title>Xmark SVG Icon</title><path fill="currentColor" d="M342.6 150.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L192 210.7L86.6 105.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L146.7 256L41.4 361.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0L192 301.3l105.4 105.3c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L237.3 256z"/></svg>`;
const adBadgeIcon = `<svg xmlns="http://www.w3.org/2000/svg" width="576" height="512" viewBox="0 0 576 512"><title>Audio-description SVG Icon</title><path fill="currentColor" d="M64 32C28.7 32 0 60.7 0 96v320c0 35.3 28.7 64 64 64h448c35.3 0 64-28.7 64-64V96c0-35.3-28.7-64-64-64zm149.5 141.3l72 144c5.9 11.9 1.1 26.3-10.7 32.2s-26.3 1.1-32.2-10.7l-9.4-18.9h-82.3l-9.4 18.9c-5.9 11.9-20.3 16.7-32.2 10.7s-16.7-20.3-10.7-32.2l72-144c4.1-8.1 12.4-13.3 21.5-13.3s17.4 5.1 21.5 13.3zm-.4 106.6L192 237.7l-21.1 42.2zM304 184c0-13.3 10.7-24 24-24h56c53 0 96 43 96 96s-43 96-96 96h-56c-13.3 0-24-10.7-24-24zm48 24v96h32c26.5 0 48-21.5 48-48s-21.5-48-48-48z"/></svg>`;
const adMuteIcon = `<svg xmlns="http://www.w3.org/2000/svg" width="576" height="512" viewBox="0 0 576 512"><title>Volume-xmark SVG Icon</title><path fill="currentColor" d="M301.1 34.8C312.6 40 320 51.4 320 64v384c0 12.6-7.4 24-18.9 29.2s-25 3.1-34.4-5.3L131.8 352H64c-35.3 0-64-28.7-64-64v-64c0-35.3 28.7-64 64-64h67.8L266.7 40.1c9.4-8.4 22.9-10.4 34.4-5.3M425 167l55 55l55-55c9.4-9.4 24.6-9.4 33.9 0s9.4 24.6 0 33.9l-55 55l55 55c9.4 9.4 9.4 24.6 0 33.9s-24.6 9.4-33.9 0l-55-55l-55 55c-9.4 9.4-24.6 9.4-33.9 0s-9.4-24.6 0-33.9l55-55l-55-55c-9.4-9.4-9.4-24.6 0-33.9s24.6-9.4 33.9 0"/></svg>`;
const actionIcons = {
  settings: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Cog-loop SVG Icon</title><defs><symbol id="lineMdCogLoop0"><path fill="none" stroke-width="2" d="M15.24 6.37C15.65 6.6 16.04 6.88 16.38 7.2C16.6 7.4 16.8 7.61 16.99 7.83C17.46 8.4 17.85 9.05 18.11 9.77C18.2 10.03 18.28 10.31 18.35 10.59C18.45 11.04 18.5 11.52 18.5 12"><animate fill="freeze" attributeName="d" begin="0.8s" dur="0.2s" values="M15.24 6.37C15.65 6.6 16.04 6.88 16.38 7.2C16.6 7.4 16.8 7.61 16.99 7.83C17.46 8.4 17.85 9.05 18.11 9.77C18.2 10.03 18.28 10.31 18.35 10.59C18.45 11.04 18.5 11.52 18.5 12;M15.24 6.37C15.65 6.6 16.04 6.88 16.38 7.2C16.38 7.2 19 6.12 19.01 6.14C19.01 6.14 20.57 8.84 20.57 8.84C20.58 8.87 18.35 10.59 18.35 10.59C18.45 11.04 18.5 11.52 18.5 12"/></path></symbol></defs><g fill="none" stroke="currentColor" stroke-width="2"><g stroke-linecap="round" stroke-linejoin="round"><path stroke-dasharray="42" stroke-dashoffset="42" d="M12 5.5C15.59 5.5 18.5 8.41 18.5 12C18.5 15.59 15.59 18.5 12 18.5C8.41 18.5 5.5 15.59 5.5 12C5.5 8.41 8.41 5.5 12 5.5z" opacity="0"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.2s" dur="0.5s" values="42;0"/><set attributeName="opacity" begin="0.2s" to="1"/><set attributeName="opacity" begin="0.7s" to="0"/></path><path stroke-dasharray="20" stroke-dashoffset="20" d="M12 9C13.66 9 15 10.34 15 12C15 13.66 13.66 15 12 15C10.34 15 9 13.66 9 12C9 10.34 10.34 9 12 9z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.2s" values="20;0"/></path></g><g opacity="0"><use href="#lineMdCogLoop0"/><use href="#lineMdCogLoop0" transform="rotate(60 12 12)"/><use href="#lineMdCogLoop0" transform="rotate(120 12 12)"/><use href="#lineMdCogLoop0" transform="rotate(180 12 12)"/><use href="#lineMdCogLoop0" transform="rotate(240 12 12)"/><use href="#lineMdCogLoop0" transform="rotate(300 12 12)"/><set attributeName="opacity" begin="0.7s" to="1"/><animateTransform attributeName="transform" dur="30s" repeatCount="indefinite" type="rotate" values="0 12 12;360 12 12"/></g></g></svg>`,
  profile: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Person SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path stroke-dasharray="20" stroke-dashoffset="20" d="M12 5C13.66 5 15 6.34 15 8C15 9.65685 13.6569 11 12 11C10.3431 11 9 9.65685 9 8C9 6.34315 10.3431 5 12 5z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.4s" values="20;0"/></path><path stroke-dasharray="36" stroke-dashoffset="36" d="M12 14C16 14 19 16 19 17V19H5V17C5 16 8 14 12 14z" opacity="0"><set attributeName="opacity" begin="0.5s" to="1"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.5s" dur="0.4s" values="36;0"/></path></g></svg>`,
  logout: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Compass SVG Icon</title><mask id="lineMdCompass0"><path fill="none" stroke="#fff" stroke-dasharray="60" stroke-dashoffset="60" stroke-linecap="round" stroke-width="2" d="M12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="60;0"/></path><path fill="#fff" d="M11 11L12 12L13 13L12 12z"><set attributeName="opacity" begin="0.6s" to="1"/><animate fill="freeze" attributeName="d" begin="0.6s" dur="0.3s" values="M11 11L12 12L13 13L12 12z;M10.2 10.2L17 7L13.8 13.8L7 17z"/><animateTransform attributeName="transform" begin="0.5s" dur="0.5s" type="rotate" values="-180 12 12;0 12 12"/></path><circle cx="12" cy="12" r="1" fill-opacity="0"><animate fill="freeze" attributeName="fill-opacity" begin="0.8s" dur="0.3s" values="0;1"/></circle></mask><rect width="24" height="24" fill="currentColor" mask="url(#lineMdCompass0)"/></svg>`,
  logout2: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Logout SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2"><path stroke-dasharray="32" stroke-dashoffset="32" d="M12 4H5C4.44772 4 4 4.44772 4 5V19C4 19.5523 4.44772 20 5 20H12"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.4s" values="32;0"/></path><path stroke-dasharray="12" stroke-dashoffset="12" d="M9 12h11.5" opacity="0"><set attributeName="opacity" begin="0.5s" to="1"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.5s" dur="0.2s" values="12;0"/></path><path stroke-dasharray="6" stroke-dashoffset="6" d="M20.5 12l-3.5 -3.5M20.5 12l-3.5 3.5" opacity="0"><set attributeName="opacity" begin="0.7s" to="1"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.7s" dur="0.2s" values="6;0"/></path></g></svg>`,
  createPoints: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>My-location-loop SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2"><path stroke-dasharray="56" stroke-dashoffset="56" d="M12 4C16.4183 4 20 7.58172 20 12C20 16.4183 16.4183 20 12 20C7.58172 20 4 16.4183 4 12C4 7.58172 7.58172 4 12 4Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="56;0"/></path><path d="M12 4v0M20 12h0M12 20v0M4 12h0" opacity="0"><set attributeName="opacity" begin="0.9s" to="1"/><animate fill="freeze" attributeName="d" begin="0.9s" dur="0.2s" values="M12 4v0M20 12h0M12 20v0M4 12h0;M12 4v-2M20 12h2M12 20v2M4 12h-2"/><animateTransform attributeName="transform" dur="30s" repeatCount="indefinite" type="rotate" values="0 12 12;360 12 12"/></path></g><circle cx="12" cy="12" r="0" fill="currentColor" fill-opacity="0"><set attributeName="fill-opacity" begin="0.6s" to="1"/><animate fill="freeze" attributeName="r" begin="0.6s" dur="0.2s" values="0;4"/></circle></svg>`,
  createMath: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Text-box-multiple SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path stroke-dasharray="62" stroke-dashoffset="62" d="M22 4V3C22 2.45 21.55 2 21 2H7C6.45 2 6 2.45 6 3V17C6 17.55 6.45 18 7 18H21C21.55 18 22 17.55 22 17z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.6s" values="62;124"/></path><g stroke-dasharray="10" stroke-dashoffset="10"><path d="M10 6h8"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.7s" dur="0.2s" values="10;0"/></path><path d="M10 10h8"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.9s" dur="0.2s" values="10;0"/></path></g><path stroke-dasharray="7" stroke-dashoffset="7" d="M10 14h5"><animate fill="freeze" attributeName="stroke-dashoffset" begin="1.1s" dur="0.2s" values="7;0"/></path><path stroke-dasharray="34" stroke-dashoffset="34" d="M2 6V21C2 21.55 2.45 22 3 22H18"><animate fill="freeze" attributeName="stroke-dashoffset" begin="1.4s" dur="0.4s" values="34;68"/></path></g></svg>`,
  createComposite: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Arrows-horizontal SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><path stroke-dasharray="12" stroke-dashoffset="12" d="M15 7H3.5M9 17H20.5"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.3s" values="12;0"/></path><path stroke-dasharray="8" stroke-dashoffset="8" d="M3 7L7 11M3 7L7 3M21 17L17 21M21 17L17 13"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.3s" dur="0.2s" values="8;0"/></path></g></svg>`,
  importJson: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Document-list SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><g stroke-width="2"><path stroke-dasharray="64" stroke-dashoffset="64" d="M13 3L19 9V21H5V3H13"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.6s" values="64;0"/></path><path stroke-dasharray="6" stroke-dashoffset="6" d="M9 13H13"><animate fill="freeze" attributeName="stroke-dashoffset" begin="1s" dur="0.2s" values="6;0"/></path><path stroke-dasharray="8" stroke-dashoffset="8" d="M9 16H15"><animate fill="freeze" attributeName="stroke-dashoffset" begin="1.2s" dur="0.2s" values="8;0"/></path></g><path stroke-dasharray="14" stroke-dashoffset="14" d="M12.5 3V8.5H19"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.7s" dur="0.2s" values="14;0"/></path></g></svg>`,
  export: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Download SVG Icon</title><path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 21h12M12 3v14m0 0l5-5m-5 5l-5-5"/></svg>`,
  addPlus: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Add-plus SVG Icon</title><path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 12h6m0 0h6m-6 0v6m0-6V6"/></svg>`,
  confirm: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Confirm SVG Icon</title><path fill="none" stroke="currentColor" stroke-dasharray="24" stroke-dashoffset="24" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 11L11 17L21 7"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.4s" values="24;0"/></path></svg>`,
  remove: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Remove SVG Icon</title><g fill="none" stroke="currentColor" stroke-dasharray="22" stroke-dashoffset="22" stroke-linecap="round" stroke-width="2"><path d="M19 5L5 19"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.3s" dur="0.3s" values="22;0"/></path><path d="M5 5L19 19"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.3s" values="22;0"/></path></g></svg>`,
  open: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Arrow-up-right-md SVG Icon</title><path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 17L17 7m0 0H9m8 0v8"/></svg>`,
};

const faviconIcons = {
  auth: '/favicons/chair.svg',
  default: '/favicons/mug.svg',
  settings: '/favicons/cog.svg',
  profile: '/favicons/smile.svg',
  newFunction: '/favicons/pen.svg',
};

const i18n = {
  ru: {
    authTitle: 'Калькулятор функций — вход',
    authSubtitle: 'Создавайте функции, управляйте точками и вычисляйте значения',
    login: 'Вход',
    register: 'Регистрация',
    nickname: 'Ник',
    password: 'Пароль',
    email: 'Email',
    repeatPassword: 'Повтор пароля',
    loginAction: 'Войти',
    registerAction: 'Зарегистрироваться',
    headerTitle: 'Калькулятор функций',
    pageTitle: 'Калькулятор функций',
    pageTitleProfile: 'Калькулятор функций — Профиль',
    pageTitleSettings: 'Калькулятор функций — Настройки',
    pageTitleAuth: 'Калькулятор функций — Вход',
    pageTitleNewFunction: 'Калькулятор функций — Новая функция',
    loggedAs: 'Вы вошли как',
    refresh: 'Обновить',
    settings: 'Настройки',
    logout: 'Выйти',
    quickActions: 'Быстрые действия',
    createFromPoints: 'Создать из точек',
    createFromMath: 'Создать из функции',
    createComposite: 'Создать сложную',
    importJson: 'Импорт JSON',
    functions: 'Функции',
    noData: 'Нет данных',
    pointsTitle: 'Точки функции',
    export: 'Экспорт',
    addPoint: 'Добавить точку',
    graph: 'График',
    evalPlaceholder: 'x для расчёта',
    complexHint: 'Композит сохраняется через /api/composite-functions и может использоваться в другой ветке.',
    modalCreateTitle: 'Создание из точек',
    name: 'Имя функции',
    type: 'Тип',
    xFrom: 'x от',
    xTo: 'x до',
    points: 'Точки',
    addPointRow: 'Добавить точку',
    create: 'Создать',
    modalMathTitle: 'Создание из аналитической функции',
    functionLabel: 'Функция',
    pointsCount: 'Количество точек',
    constValue: 'Значение константы',
    cancel: 'Отмена',
    close: 'Закрыть',
    modalCompositeTitle: 'Создание сложной функции',
    firstFn: 'Первая функция (f)',
    secondFn: 'Вторая функция (g)',
    save: 'Сохранить',
    settingsTitle: 'Настройки',
    theme: 'Тема',
    edges: 'Скругление',
    language: 'Язык',
    extraFeatures: 'Специальные возможности',
    inclusive: 'Контраст и крупный текст',
    adblock: 'AdBlock',
    newYearMode: 'Новогодний режим',
    tickerEnabled: 'Лента новостей',
    successLabel: 'Успешно',
    warningLabel: 'Предупреждение',
    infoLabel: 'Инфо',
    errorLabel: 'Ошибка',
    themes: {
      dark: 'Тёмная',
      light: 'Светлая',
      neo: 'Нео',
      sunset: 'Закат',
      ocean: 'Океан',
      forest: 'Лес',
      candy: 'Кэнди',
      mono: 'Моно',
      cyber: 'Кибер',
    },
    edgesDict: {
      round: 'Кругло',
      soft: 'Мягко',
      rough: 'Остро',
    },
    error: 'Ошибка',
    ok: 'Понятно',
    contact: 'Контакты',
    footerLinks: ['Документация','Поддержка','Политика','Контакты','О проекте','Помощь','API','Скачать'],
    langRu: 'Русский',
    langEn: 'English',
    delete: 'Удалить',
    open: 'Открыть',
    profile: 'Профиль',
    update: 'Обновить',
    deleteAccount: 'Удалить аккаунт',
    passwordNew: 'Новый пароль',
    toastRegisterSuccess: 'Регистрация успешна, войдите под новым пользователем',
    toastConnectionEstablished: 'Готово! Соединение установлено',
    errorFillLoginPassword: 'Заполните логин и пароль',
    errorEnterLoginPassword: 'Введите логин и пароль',
    errorPasswordsNotMatch: 'Пароли не совпадают',
    errorUsernameTooLong: 'Логин не длиннее 32 символов.',
    errorEmailTooLong: 'Email не длиннее 128 символов.',
    errorPasswordTooLong: 'Пароль не длиннее 128 символов.',
    errorRegistrationFailed: 'Ошибка регистрации: проверьте логин/email/пароль.',
    errorLoginFailed: 'Не удалось войти',
    errorWrongLoginPassword: 'Неверный логин или пароль',
    errorUsernameFormat: 'Логин 3-32 символа, только буквы/цифры/_/-',
    errorEmailInvalid: 'Введите корректный email.',
    errorPasswordTooShort: 'Пароль должен быть не короче 6 символов.',
    errorPasswordWeak: 'Пароль должен содержать прописные, строчные, цифру и спецсимвол (6-128).',
    errorUserIdUnknown: 'UserId не определён. Перелогиньтесь.',
    errorUsernameTaken: 'Такой логин уже занят.',
    toastFunctionDeleted: 'Функция удалена',
    toastCompositeChildError: 'Не удалось получить дочерние функции для композиции',
    toastCompositeOutOfRange: 'Композиция вне области определения: скорректируйте диапазоны',
    toastCompositeCalcError: 'Не удалось вычислить композицию',
    toastNoPoints: 'Для функции нет точек. Укажите xFrom/xTo или создайте точки.',
    toastCompositePointsChange: 'Точки композиции меняются через дочерние функции',
    toastPointUpdated: 'Точка обновлена',
    toastPointSaved: 'Точка сохранена',
    toastNoFunctionSelected: 'Нет выбранной функции для сохранения точки',
    toastDeleteInBaseFunctions: 'Удаляйте точки в базовых функциях, не в композиции',
    toastPointDeleted: 'Точка удалена',
    toastLocalPointDeleted: 'Локальная точка удалена',
    toastFunctionSaved: 'Функция сохранена',
    toastFunctionFromFormula: 'Функция по формуле создана',
    toastCompositeSaved: 'Композит сохранён',
    toastAddPointsInChild: 'Добавляйте точки в дочерние функции, не в композицию',
    toastSettingsSaved: 'Настройки сохранены',
    toastSaveInBaseFunctions: 'Сохраняйте точки в базовых функциях, композиция только читает',
    toastExportedJson: 'Экспортировано в JSON',
    toastPointsLoaded: 'Точки загружены в форму создания',
    errorLoadingFunctions: 'Загрузка функций',
    errorDeletingFunction: 'Удаление функции',
    errorFunctionPoints: 'Точки функции',
    errorUpdatingPoint: 'Обновление точки',
    errorDeletingPoint: 'Удаление точки',
    errorCreatingFunction: 'Создание функции',
    errorCreatingFromFormula: 'Создание по формуле',
    errorCompositeFunction: 'Композитная функция',
    errorLoadingProfile: 'Не удалось загрузить профиль',
    errorDeleting: 'Удаление',
    errorImport: 'Импорт',
    types: {
      SQR: 'Квадратичная',
      IDENTITY: 'Тождественная',
      CONSTANT: 'Константа',
      CONST: 'Константа',
      LINEAR: 'Линейная',
      UNIT: 'Единичная',
      ZERO: 'Нулевая',
      COS: 'Косинус',
      SIN: 'Синус',
      TAN: 'Тангенс',
      CTG: 'Котангенс',
      EXP: 'Экспонента',
      LOG: 'Логарифм',
      LN: 'Нат. логарифм',
      LOG10: 'Десят. логарифм',
      LOGA: 'Логарифм по основанию a',
      CUBE: 'Кубическая',
      SQRT: 'Кв. корень',
      CBRT: 'Куб. корень',
      RECIP: 'Обратная',
      ABS: 'Модуль',
      COMPOSITE: 'Сложная',
      TABULATED: 'Табулированная',
      MATH: 'Аналитическая',
    },
  },
  en: {
    authTitle: 'Function Calculator — Sign in',
    authSubtitle: 'Create functions, manage points, and calculate values',
    login: 'Login',
    register: 'Register',
    nickname: 'Username',
    password: 'Password',
    email: 'Email',
    repeatPassword: 'Repeat password',
    loginAction: 'Sign in',
    registerAction: 'Sign up',
    headerTitle: 'Function Calculator',
    pageTitle: 'Function Calculator',
    pageTitleProfile: 'Function Calculator — Profile',
    pageTitleSettings: 'Function Calculator — Settings',
    pageTitleAuth: 'Function Calculator — Sign in',
    pageTitleNewFunction: 'Function Calculator — New Function',
    loggedAs: 'Signed in as',
    refresh: 'Refresh',
    settings: 'Settings',
    logout: 'Logout',
    quickActions: 'Quick actions',
    createFromPoints: 'Create from points',
    createFromMath: 'Create from function',
    createComposite: 'Create composite',
    importJson: 'Import JSON',
    functions: 'Functions',
    noData: 'No data',
    pointsTitle: 'Function points',
    export: 'Export',
    addPoint: 'Add point',
    graph: 'Chart',
    evalPlaceholder: 'x to evaluate',
    complexHint: 'Composite saved via /api/composite-functions and reused elsewhere.',
    modalCreateTitle: 'Create from points',
    name: 'Function name',
    type: 'Type',
    xFrom: 'x from',
    xTo: 'x to',
    points: 'Points',
    addPointRow: 'Add point',
    create: 'Create',
    modalMathTitle: 'Create from analytic function',
    functionLabel: 'Function',
    pointsCount: 'Points count',
    constValue: 'Constant value',
    cancel: 'Cancel',
    close: 'Close',
    modalCompositeTitle: 'Create composite function',
    firstFn: 'First function (f)',
    secondFn: 'Second function (g)',
    save: 'Save',
    settingsTitle: 'Settings',
    theme: 'Theme',
    edges: 'Corner style',
    language: 'Language',
    extraFeatures: 'Accessability & Extra features',
    inclusive: 'High contrast mode',
    adblock: 'AdBlock',
    newYearMode: 'Christmas mode',
    tickerEnabled: 'News ticker',
    successLabel: 'Success',
    warningLabel: 'Warning',
    infoLabel: 'Info',
    errorLabel: 'Error',
    themes: {
      dark: 'Dark',
      light: 'Light',
      neo: 'Neo',
      sunset: 'Sunset',
      ocean: 'Ocean',
      forest: 'Forest',
      candy: 'Candy',
      mono: 'Mono',
      cyber: 'Cyber'
    },
    edgesDict: {
      round: 'Round',
      soft: 'Soft',
      rough: 'Sharp',
    },
    error: 'Error',
    ok: 'Got it',
    contact: 'Contact',
    footerLinks: ['Docs','Support','Policy','Contacts','About','Help','API','Download'],
    langRu: 'Русский',
    langEn: 'English',
    delete: 'Delete',
    open: 'Open',
    profile: 'Profile',
    update: 'Update',
    deleteAccount: 'Delete account',
    passwordNew: 'New password',
    toastRegisterSuccess: 'Registration successful, please sign in with your new account',
    toastConnectionEstablished: 'Done! Connection established',
    errorFillLoginPassword: 'Please enter username and password',
    errorEnterLoginPassword: 'Enter username and password',
    errorPasswordsNotMatch: 'Passwords do not match',
    errorUsernameTooLong: 'Username must be at most 32 characters.',
    errorEmailTooLong: 'Email must be at most 128 characters.',
    errorPasswordTooLong: 'Password must be at most 128 characters.',
    errorRegistrationFailed: 'Registration error: check login/email/password.',
    errorLoginFailed: 'Failed to login',
    errorWrongLoginPassword: 'Wrong login or password',
    errorUsernameFormat: 'Username must be 3-32 chars, letters/digits/_/- only.',
    errorEmailInvalid: 'Enter a valid email.',
    errorPasswordTooShort: 'Password must be at least 6 characters.',
    errorPasswordWeak: 'Password must have upper, lower, digit and special char (6-128).',
    errorUserIdUnknown: 'User id is unknown. Re-login, please.',
    errorUsernameTaken: 'This username is already taken.',
    toastFunctionDeleted: 'Function deleted',
    toastCompositeChildError: 'Failed to get child functions for composition',
    toastCompositeOutOfRange: 'Composition out of domain: adjust ranges',
    toastCompositeCalcError: 'Failed to calculate composition',
    toastNoPoints: 'Function has no points. Specify xFrom/xTo or create points.',
    toastCompositePointsChange: 'Composite points are changed through child functions',
    toastPointUpdated: 'Point updated',
    toastPointSaved: 'Point saved',
    toastNoFunctionSelected: 'No function selected to save point',
    toastDeleteInBaseFunctions: 'Delete points in base functions, not in composition',
    toastPointDeleted: 'Point deleted',
    toastLocalPointDeleted: 'Local point deleted',
    toastFunctionSaved: 'Function saved',
    toastFunctionFromFormula: 'Function from formula created',
    toastCompositeSaved: 'Composite saved',
    toastAddPointsInChild: 'Add points in child functions, not in composition',
    toastSettingsSaved: 'Settings saved',
    toastSaveInBaseFunctions: 'Save points in base functions, composition is read-only',
    toastExportedJson: 'Exported to JSON',
    toastPointsLoaded: 'Points loaded into creation form',
    errorLoadingFunctions: 'Loading functions',
    errorDeletingFunction: 'Deleting function',
    errorFunctionPoints: 'Function points',
    errorUpdatingPoint: 'Updating point',
    errorDeletingPoint: 'Deleting point',
    errorCreatingFunction: 'Creating function',
    errorCreatingFromFormula: 'Creating from formula',
    errorCompositeFunction: 'Composite function',
    errorLoadingProfile: 'Failed to load profile',
    errorDeleting: 'Deletion',
    errorImport: 'Import',
    types: {
      SQR: 'Square',
      IDENTITY: 'Identity',
      CONSTANT: 'Constant',
      CONST: 'Constant',
      LINEAR: 'Linear',
      UNIT: 'Unit',
      ZERO: 'Zero',
      COS: 'Cosine',
      SIN: 'Sine',
      TAN: 'Tangent',
      CTG: 'Cotangent',
      EXP: 'Exponential',
      LOG: 'Logarithm',
      LN: 'Natural log',
      LOG10: 'Decimal log',
      LOGA: 'Log base a',
      CUBE: 'Cubic',
      SQRT: 'Sqrt',
      CBRT: 'Cbrt',
      RECIP: 'Reciprocal',
      ABS: 'Absolute',
      COMPOSITE: 'Composite',
      TABULATED: 'Tabulated',
      MATH: 'Analytic',
    },
  }
};

async function loadTemplate() {
  const res = await fetch('/template.html');
  return res.text();
}

function normalizeFunction(raw = {}) {
  const type = (raw.functionType ?? raw.function_type ?? raw.type ?? '').toUpperCase();
  const compositeIdRaw = raw.compositeId ?? raw.composite_id;
  const parsedId = Number(raw.functionId ?? raw.function_id ?? raw.id ?? compositeIdRaw);
  const firstIdRaw = raw.firstFunctionId ?? raw.first_function_id ?? raw.first_functionId;
  const secondIdRaw = raw.secondFunctionId ?? raw.second_function_id ?? raw.second_functionId;
  const compositeId = Number.isFinite(Number(compositeIdRaw)) ? Number(compositeIdRaw) : compositeIdRaw;
  const normalized = {
    ...raw,
    compositeId,
    functionId: Number.isFinite(parsedId) ? parsedId : (raw.functionId ?? raw.function_id ?? raw.id ?? compositeIdRaw ?? raw.composite_id),
    functionName: raw.functionName ?? raw.function_name ?? raw.name ?? raw.compositeName ?? raw.composite_name,
    functionType: type || raw.functionType || raw.function_type,
    xFrom: raw.xFrom ?? raw.x_from ?? raw.xfrom ?? raw.xFromValue,
    xTo: raw.xTo ?? raw.x_to ?? raw.xto ?? raw.xToValue,
    firstFunctionId: Number.isFinite(Number(firstIdRaw)) ? Number(firstIdRaw) : firstIdRaw,
    secondFunctionId: Number.isFinite(Number(secondIdRaw)) ? Number(secondIdRaw) : secondIdRaw,
  };
  const isComposite = type === 'COMPOSITE' || normalized.firstFunctionId || normalized.secondFunctionId || raw._composite;
  return { ...normalized, _composite: Boolean(isComposite) };
}

function dedupeByX(points) {
  const seen = new Map();
  for (const p of points) {
    const x = Number(p.xValue);
    if (!Number.isFinite(x)) continue;
    if (!seen.has(x)) {
      seen.set(x, p);
    }
  }
  return Array.from(seen.values()).sort((a, b) => Number(a.xValue) - Number(b.xValue));
}

function pickRandomAds(count = 2) {
  if (!adInventory.length) return [];
  const pool = [...adInventory];
  const result = [];
  for (let i = 0; i < count; i++) {
    if (!pool.length) pool.push(...adInventory);
    const idx = Math.floor(Math.random() * pool.length);
    const [picked] = pool.splice(idx, 1);
    if (picked) result.push(picked);
  }
  return result;
}

function pickRandomLogo() {
  if (!logos.length) return 'https://media.tenor.com/RnC4v5oEP34AAAAi/bmw-logo.gif';
  const idx = Math.floor(Math.random() * logos.length);
  return logos[idx] || 'https://media.tenor.com/RnC4v5oEP34AAAAi/bmw-logo.gif';
}

async function fetchPointsNormalized(functionId, depth = 0) {
  if (depth > 3) return [];
  
  try {
    const fnRes = await api.get(`/functions/${functionId}`);
    const fnType = (fnRes?.functionType || fnRes?.function_type || '').toUpperCase();
    if (fnType === 'COMPOSITE') {
      let firstId = fnRes?.firstFunctionId || fnRes?.first_function_id || fnRes?.first_functionId;
      let secondId = fnRes?.secondFunctionId || fnRes?.second_function_id || fnRes?.second_functionId;
      if (!firstId || !secondId) {
        try {
          const comp = await api.get(`/composite-functions/${functionId}/points`);
          firstId = firstId || comp?.firstFunctionId || comp?.first_function_id;
          secondId = secondId || comp?.secondFunctionId || comp?.second_function_id;
        } catch (err) {
          console.warn('fetch composite ids failed', err);
        }
      }
      if (!firstId || !secondId) {
        console.warn('compose: missing child ids for composite', functionId, { firstId, secondId, fnRes });
        const raw = await api.get(`/functions/${functionId}/points`);
        let points = (raw || [])
          .map(normalizePoint)
          .map((p, idx) => ({
            ...p,
            xValue: p.xValue,
            yValue: p.yValue,
            pointId: p.pointId ?? p.id ?? `local-${idx}`,
            functionId: p.functionId ?? functionId,
          }));
        if (points.length) {
          return dedupeByX(points);
        }
        return [];
      } else {
        console.log(`[fetchPointsNormalized] Computing composite for functionId=${functionId}, firstId=${firstId}, secondId=${secondId}`);
        const composed = await composeFunctions(firstId, secondId, depth + 1);
        if (composed?.length) {
          console.log(`[fetchPointsNormalized] Composite computed successfully: ${composed.length} points`);
          return dedupeByX(composed);
        }
        console.warn(`[fetchPointsNormalized] Composite composition returned no points for functionId=${functionId}`);
        return [];
      }
    }
  } catch (e) {
    console.warn('fetchPointsNormalized error:', e);
  }
  
  let fnType = null;
  try {
    const fnRes = await api.get(`/functions/${functionId}`);
    fnType = (fnRes?.functionType || fnRes?.function_type || '').toUpperCase();
  } catch (e) {
    console.warn('fetchPointsNormalized: failed to get function type', e);
  }
  
  let points = [];
  try {
    const raw = await api.get(`/functions/${functionId}/points`);
    points = (raw || [])
      .map(normalizePoint)
      .map((p, idx) => ({
        ...p,
        xValue: p.xValue,
        yValue: p.yValue,
        pointId: p.pointId ?? p.id ?? `local-${idx}`,
        functionId: p.functionId ?? functionId,
      }));
    points = dedupeByX(points);
    if (!points.length && fnType && fnType !== 'TABULATED' && fnType !== 'COMPOSITE') {
      let fnRes = null;
      try {
        fnRes = await api.get(`/functions/${functionId}`);
      } catch (e) {
        console.warn('fetchPointsNormalized: failed to get function for synthesis', e);
      }
      let xFrom = fnRes?.xFrom ?? fnRes?.x_from;
      let xTo = fnRes?.xTo ?? fnRes?.x_to;
      if (xFrom === undefined || xFrom === null || xTo === undefined || xTo === null) {
        xFrom = -5;
        xTo = 5;
      }
      if (Number(xFrom) === Number(xTo)) {
        xFrom = Number(xFrom) - 5;
        xTo = Number(xTo) + 5;
      }
      const synth = synthesizePointsFromFunction({
        functionId,
        functionType: fnType,
        xFrom,
        xTo,
      }, 200);
      points = synth.map((p, idx) => ({
        ...p,
        pointId: p.pointId ?? `synth-${idx}`,
        functionId,
      }));
    }
    
    try {
      return dedupeByX(points);
    } catch {
      return points;
    }
  } catch (e) {
    console.warn('fetchPointsNormalized fallback failed', e);
    return [];
  }
  
  try {
    return dedupeByX(points);
  } catch {
    return points;
  }
}

async function composeFunctions(firstId, secondId, depth = 0) {
  const fPtsRaw = await fetchPointsNormalized(Number(firstId), depth);
  const fPts = fPtsRaw.map((p) => {
    const xVal = p.xValue ?? p.xvalue ?? p.x_value ?? p.x;
    const yVal = p.yValue ?? p.yvalue ?? p.y_value ?? p.y;
    return {
      ...p,
      xValue: Number(xVal),
      yValue: Number(yVal),
    };
  }).filter((p) => Number.isFinite(p.xValue) && Number.isFinite(p.yValue));
  const gPtsRaw = await fetchPointsNormalized(Number(secondId), depth);
  const gPts = gPtsRaw.map((p) => {
    const xVal = p.xValue ?? p.xvalue ?? p.x_value ?? p.x;
    const yVal = p.yValue ?? p.yvalue ?? p.y_value ?? p.y;
    return {
      ...p,
      xValue: Number(xVal),
      yValue: Number(yVal),
    };
  }).filter((p) => Number.isFinite(p.xValue) && Number.isFinite(p.yValue));
  console.log('compose source fPts', fPts, 'gPts', gPts);
  if (fPts.length > 0) console.log('compose fPts[0] sample:', { xValue: fPts[0].xValue, yValue: fPts[0].yValue, raw: fPtsRaw?.[0] });
  if (gPts.length > 0) console.log('compose gPts[0] sample:', { xValue: gPts[0].xValue, yValue: gPts[0].yValue, raw: gPtsRaw?.[0] });
  if (!fPts.length || !gPts.length) {
    console.warn('compose aborted: empty points', { firstId, secondId, fPtsLen: fPts.length, gPtsLen: gPts.length });
    return [];
  }

  const build = (inner, outer, label) => {
    if (!inner.length || !outer.length) return [];
    const innerSorted = [...inner].sort((a, b) => a.xValue - b.xValue);
    const outerSorted = [...outer].sort((a, b) => a.xValue - b.xValue);
    let innerMin = innerSorted[0].xValue;
    let innerMax = innerSorted[innerSorted.length - 1].xValue;
    const outerMin = outerSorted[0].xValue;
    const outerMax = outerSorted[outerSorted.length - 1].xValue;
    if (innerMin === innerMax) {
      innerMin -= 5;
      innerMax += 5;
    }
    const samples = Math.max(1000, inner.length * 30);
    const step = (innerMax - innerMin) / (samples - 1 || 1);
    const pts = [];
    let skippedOutOfRange = 0;
    let skippedNull = 0;
    let debugFirstFew = true;
    for (let i = 0; i < samples; i++) {
      const x = innerMin + i * step;
      const yInner = linearInterpolate(innerSorted, x);
      if (yInner === null || yInner === undefined) {
        skippedNull++;
        continue;
      }
      
      let yOuter;
      let yInnerForLookup = yInner;
      
      if (yInner < outerMin && outerMin >= 0 && outerMin < 1.0) {
        yInnerForLookup = Math.abs(yInner);
        if (yInnerForLookup <= outerMax) {
          yOuter = linearInterpolate(outerSorted, yInnerForLookup);
          skippedOutOfRange++;
          let shouldLog = skippedOutOfRange <= 10;
          if (shouldLog) {
            console.log(`[compose debug] x=${x.toFixed(4)}, f(x)=${yInner.toFixed(4)} < ${outerMin.toFixed(4)}, using reflection: g(${yInner.toFixed(4)}) = g(${yInnerForLookup.toFixed(4)}) = ${yOuter?.toFixed(4)}`);
          }
          if (yOuter !== null && yOuter !== undefined) {
            pts.push({ pointId: `comp-${pts.length}`, functionId: `comp-${firstId}-${secondId}`, xValue: x, yValue: yOuter });
            if (pts.length === 10) debugFirstFew = false;
          }
          continue;
        } else {
          if (debugFirstFew && skippedOutOfRange <= 3) {
            console.log(`[compose debug] x=${x.toFixed(4)}, f(x)=${yInner.toFixed(4)}, reflection would give ${yInnerForLookup.toFixed(4)} which is > ${outerMax.toFixed(4)}, using normal extrapolation`);
          }
        }
      }
      
      if (yInner < outerMin) {
        if (outerSorted.length >= 3) {
          const x0 = outerSorted[0].xValue;
          const y0 = outerSorted[0].yValue;
          const x2 = outerSorted[Math.min(2, outerSorted.length - 1)].xValue;
          const y2 = outerSorted[Math.min(2, outerSorted.length - 1)].yValue;
          const dx = x2 - x0;
          if (Math.abs(dx) > 1e-10) {
            const slope = (y2 - y0) / dx;
            yOuter = y0 + (yInner - x0) * slope;
          } else {
            yOuter = y0;
          }
        } else if (outerSorted.length >= 2) {
          const x0 = outerSorted[0].xValue;
          const y0 = outerSorted[0].yValue;
          const x1 = outerSorted[1].xValue;
          const y1 = outerSorted[1].yValue;
          const dx = x1 - x0;
          if (Math.abs(dx) > 1e-10) {
            const slope = (y1 - y0) / dx;
            yOuter = y0 + (yInner - x0) * slope;
          } else {
            yOuter = y0;
          }
        } else {
          yOuter = outerSorted[0].yValue;
        }
        skippedOutOfRange++;
        if (debugFirstFew && skippedOutOfRange <= 3) {
          console.log(`[compose debug] x=${x.toFixed(4)}, f(x)=${yInner.toFixed(4)} < ${outerMin.toFixed(4)}, extrapolating left: g(${yInner.toFixed(4)}) ≈ ${yOuter.toFixed(4)}`);
        }
      } else if (yInner > outerMax) {
        if (outerSorted.length >= 3) {
          const n = outerSorted.length;
          const x0 = outerSorted[Math.max(0, n - 3)].xValue;
          const y0 = outerSorted[Math.max(0, n - 3)].yValue;
          const x2 = outerSorted[n - 1].xValue;
          const y2 = outerSorted[n - 1].yValue;
          const dx = x2 - x0;
          if (Math.abs(dx) > 1e-10) {
            const slope = (y2 - y0) / dx;
            yOuter = y2 + (yInner - x2) * slope;
          } else {
            yOuter = y2;
          }
        } else if (outerSorted.length >= 2) {
          const n = outerSorted.length;
          const x0 = outerSorted[n - 2].xValue;
          const y0 = outerSorted[n - 2].yValue;
          const x1 = outerSorted[n - 1].xValue;
          const y1 = outerSorted[n - 1].yValue;
          const dx = x1 - x0;
          if (Math.abs(dx) > 1e-10) {
            const slope = (y1 - y0) / dx;
            yOuter = y1 + (yInner - x1) * slope;
          } else {
            yOuter = y1;
          }
        } else {
          yOuter = outerSorted[outerSorted.length - 1].yValue;
        }
        skippedOutOfRange++;
        if (debugFirstFew && skippedOutOfRange <= 3) {
          console.log(`[compose debug] x=${x.toFixed(4)}, f(x)=${yInner.toFixed(4)} > ${outerMax.toFixed(4)}, extrapolating right: g(${yInner.toFixed(4)}) ≈ ${yOuter.toFixed(4)}`);
        }
      } else {
        yOuter = linearInterpolate(outerSorted, yInner);
        if (debugFirstFew && pts.length < 3) {
          console.log(`[compose debug] x=${x.toFixed(4)}, f(x)=${yInner.toFixed(4)} in range [${outerMin.toFixed(4)}, ${outerMax.toFixed(4)}], interpolating: g(${yInner.toFixed(4)}) = ${yOuter.toFixed(4)}`);
        }
      }
      if (yOuter === null || yOuter === undefined) continue;
      if (debugFirstFew && pts.length < 3) {
        console.log(`[compose debug] x=${x.toFixed(4)}, f(x)=${yInner.toFixed(4)}, g(f(x))=${yOuter.toFixed(4)}`);
      }
      pts.push({ pointId: `comp-${pts.length}`, functionId: `comp-${firstId}-${secondId}`, xValue: x, yValue: yOuter });
      if (pts.length === 3) debugFirstFew = false;
    }
    console.log(`[compose ${label}] firstId=${firstId} (f) secondId=${secondId} (g), computing g(f(x))`);
    const innerYMin = Math.min(...innerSorted.map(p => p.yValue));
    const innerYMax = Math.max(...innerSorted.map(p => p.yValue));
    console.log(`[compose ${label}] inner (f) domain: [${innerMin}, ${innerMax}], outer (g) domain: [${outerMin}, ${outerMax}]`);
    console.log(`[compose ${label}] inner (f) range: [${innerYMin.toFixed(4)}, ${innerYMax.toFixed(4)}]`);
    console.log(`[compose ${label}] inner (f) points: ${inner.length}, outer (g) points: ${outer.length}`);
    if (inner.length <= 5) console.log(`[compose ${label}] inner (f) sample points:`, innerSorted.map(p => `(${p.xValue.toFixed(3)}, ${p.yValue.toFixed(3)})`));
    if (outer.length <= 5) console.log(`[compose ${label}] outer (g) sample points:`, outerSorted.map(p => `(${p.xValue.toFixed(3)}, ${p.yValue.toFixed(3)})`));
    console.log(`[compose ${label}] samples=${samples}, result=${pts.length}, extrapolatedOutOfRange=${skippedOutOfRange}, skippedNull=${skippedNull}`);
    if (skippedOutOfRange > 0) {
      console.log(`[compose ${label}] INFO: ${skippedOutOfRange} points extrapolated because f(x) values [${innerYMin.toFixed(4)}, ${innerYMax.toFixed(4)}] extend outside g's domain [${outerMin}, ${outerMax}]`);
    }
    if (firstId === secondId) {
      console.warn(`[compose ${label}] WARNING: Both functions have the same ID (${firstId})! This means you're composing a function with itself.`);
    }
    if (pts.length > 0) {
      console.log(`[compose ${label}] first few points:`, pts.slice(0, 5).map(p => `(${p.xValue.toFixed(2)}, ${p.yValue.toFixed(2)})`));
      console.log(`[compose ${label}] last few points:`, pts.slice(-5).map(p => `(${p.xValue.toFixed(2)}, ${p.yValue.toFixed(2)})`));
    } else {
      console.warn(`[compose ${label}] WARNING: No points computed! This might indicate domain mismatch.`);
    }
    return pts;
  };

  const result = build(fPts, gPts, 'g(f(x))');
  return result;
}

function persistCredentials(credentials) {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(credentials));
}

function readCredentials() {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch (e) {
    return null;
  }
}

function normalizePoint(raw) {
  const xVal = raw?.xValue ?? raw?.x_value ?? raw?.xvalue ?? raw?.x;
  const yVal = raw?.yValue ?? raw?.y_value ?? raw?.yvalue ?? raw?.y;
  const x = Number(xVal);
  const y = Number(yVal);
  return {
    ...raw,
    xValue: Number.isFinite(x) ? x : xVal,
    yValue: Number.isFinite(y) ? y : yVal,
  };
}

function linearInterpolate(points, x) {
  if (!points?.length) return null;
  const sorted = [...points].sort((a, b) => a.xValue - b.xValue);
  if (x <= sorted[0].xValue) return sorted[0].yValue;
  if (x >= sorted[sorted.length - 1].xValue) return sorted[sorted.length - 1].yValue;

  for (let i = 0; i < sorted.length - 1; i++) {
    const a = sorted[i];
    const b = sorted[i + 1];
    if (x >= a.xValue && x <= b.xValue) {
      const t = (x - a.xValue) / (b.xValue - a.xValue || 1);
      return a.yValue + t * (b.yValue - a.yValue);
    }
  }
  return null;
}

function synthesizePointsFromFunction(fn, count = 25) {
  if (!fn) return [];
  const evalKey = fn.functionType?.toUpperCase?.();
  const evaluator =
    mathEvaluators[evalKey] ||
    (evalKey === 'TABULATED' ? null : null) ||
    null;

  let exprEvaluator = null;
  if (!evaluator && fn.functionExpression) {
    try {
      exprEvaluator = new Function('x', `return ${fn.functionExpression};`);
    } catch (e) {
      exprEvaluator = null;
    }
  }

  const from = fn.xFrom ?? -5;
  const to = fn.xTo ?? 5;
  const step = (to - from) / (count - 1 || 1);
  const result = [];

  for (let i = 0; i < count; i++) {
    const x = from + i * step;
    let y = null;
    try {
      if (evaluator) {
        y = evaluator(x, fn.constantValue ?? 0);
      } else if (exprEvaluator) {
        y = exprEvaluator(x);
      }
    } catch (e) {
      y = null;
    }
    if (Number.isFinite(y)) {
      result.push({ pointId: `s-${i}`, functionId: fn.functionId, xValue: x, yValue: y });
    }
  }
  return result;
}

// Новогодний режим, управление снегом
class SnowManager {
  constructor(config) {
    this.snowflakes = [];
    this.animationId = null;
    this.container = null;
    this.isActive = false;
    this.spawnInterval = null;
    this.config = config || {
      sizeMin: SNOW_FG_SIZE_MIN,
      sizeMax: SNOW_FG_SIZE_MAX,
      speedMin: SNOW_FG_SPEED_MIN,
      speedMax: SNOW_FG_SPEED_MAX,
      count: SNOW_FG_COUNT,
      zIndex: SNOW_FG_Z_INDEX,
      opacity: SNOW_FG_OPACITY
    };
  }

  createSnowflake(spawnFromTop = false) {
    const snowflake = document.createElement('img');
    snowflake.src = SNOWFLAKE_URL;
    snowflake.style.position = 'fixed';
    snowflake.style.pointerEvents = 'none';
    snowflake.style.zIndex = this.config.zIndex;
    snowflake.style.opacity = String(this.config.opacity);
    
    const size = Math.random() * (this.config.sizeMax - this.config.sizeMin) + this.config.sizeMin;
    snowflake.style.width = `${size}px`;
    snowflake.style.height = `${size}px`;
    
    snowflake.style.left = `${Math.random() * window.innerWidth}px`;
    
    if (spawnFromTop) {
      const windowHeight = window.innerHeight;
      const spawnRangeMin = this.config.sizeMax;
      const spawnRangeMax = windowHeight;
      snowflake.style.top = `${-spawnRangeMin - Math.random() * spawnRangeMax}px`;
    } else {
      snowflake.style.top = `-${this.config.sizeMax}px`;
    }
    
    const speed = Math.random() * (this.config.speedMax - this.config.speedMin) + this.config.speedMin;
    snowflake.dataset.speed = speed;
    
    const rotation = Math.random() * 360;
    snowflake.style.transform = `rotate(${rotation}deg)`;
    snowflake.dataset.rotation = rotation;
    const spinDirection = Math.random() < 0.5 ? -1 : 1;
    snowflake.dataset.rotationSpeed = spinDirection * (Math.random() * speed/5 + this.config.speedMin/2);
    
    return snowflake;
  }

  animate() {
    if (!this.isActive) return;
    
    const windowHeight = window.innerHeight;
    const windowWidth = window.innerWidth;
    
    this.snowflakes.forEach((flake) => {
      const currentTop = parseFloat(flake.style.top) || 0;
      const speed = parseFloat(flake.dataset.speed);
      const newTop = currentTop + speed;
      
      flake.style.top = `${newTop}px`;
      
      const rotation = parseFloat(flake.dataset.rotation) + parseFloat(flake.dataset.rotationSpeed);
      flake.dataset.rotation = rotation;
      flake.style.transform = `rotate(${rotation}deg)`;
      
      if (newTop > windowHeight) {
        flake.style.top = `-${this.config.sizeMax}px`;
        flake.style.left = `${Math.random() * windowWidth}px`;
        const size = Math.random() * (this.config.sizeMax - this.config.sizeMin) + this.config.sizeMin;
        flake.style.width = `${size}px`;
        flake.style.height = `${size}px`;
        const newSpeed = Math.random() * (this.config.speedMax - this.config.speedMin) + this.config.speedMin;
        flake.dataset.speed = newSpeed;
      }
      
      const currentLeft = parseFloat(flake.style.left) || 0;
      if (currentLeft > windowWidth) {
        flake.style.left = `${Math.random() * windowWidth}px`;
      }
    });
    
    this.animationId = requestAnimationFrame(() => this.animate());
  }

  start() {
    if (this.isActive) return;
    
    this.isActive = true;
    
    if (!this.container) {
      this.container = document.createElement('div');
      this.container.id = `snow-container-${this.config.zIndex}`;
      this.container.style.position = 'fixed';
      this.container.style.top = '0';
      this.container.style.left = '0';
      this.container.style.width = '100%';
      this.container.style.height = '100%';
      this.container.style.pointerEvents = 'none';
      this.container.style.zIndex = this.config.zIndex;
      document.body.appendChild(this.container);
    }
    
    this.snowflakes = [];
    this.animate();
    
    let created = 0;
    this.spawnInterval = setInterval(() => {
      if (!this.isActive || created >= this.config.count) {
        if (this.spawnInterval) {
          clearInterval(this.spawnInterval);
          this.spawnInterval = null;
        }
        return;
      }
      const flake = this.createSnowflake(true);
      this.container.appendChild(flake);
      this.snowflakes.push(flake);
      created++;
    }, 200);
  }

  stop() {
    this.isActive = false;
    
    if (this.animationId) {
      cancelAnimationFrame(this.animationId);
      this.animationId = null;
    }
    
    if (this.spawnInterval) {
      clearInterval(this.spawnInterval);
      this.spawnInterval = null;
    }
    
    if (this.container) {
      this.container.remove();
      this.container = null;
      this.snowflakes = [];
    }
  }
}

// Рождественские огоньки - управление
class ChristmasLightsManager {
  constructor() {
    this.container = null;
    this.lights = [];
    this.animationId = null;
    this.isActive = false;
    this.colorIndex = 0;
    this.startTime = 0;
  }

  createLight(position) {
    const light = document.createElement('div');
    light.style.position = 'fixed';
    light.style.pointerEvents = 'none';
    light.style.zIndex = CHRISTMAS_LIGHTS_Z_INDEX;
    light.style.width = `${CHRISTMAS_LIGHTS_SIZE}px`;
    light.style.height = `${CHRISTMAS_LIGHTS_SIZE}px`;
    light.style.borderRadius = '50%';
    light.style.opacity = CHRISTMAS_LIGHTS_OPACITY;
    light.style.transition = 'background 0.325s ease';
    
    if (position === 'left') {
      light.style.bottom = '0';
      light.style.left = '0';
      light.style.transform = 'translate(-50%, 50%)';
    } else if (position === 'right') {
      light.style.bottom = '0';
      light.style.right = '0';
      light.style.transform = 'translate(50%, 50%)';
    } else {
      light.style.bottom = '0';
      light.style.left = '50%';
      light.style.transform = 'translate(-50%, 50%)';
    }
    
    return light;
  }

  updateGradients() {
    if (!this.isActive) return;
    
    const elapsed = Date.now() - this.startTime;
    const progress = (elapsed % CHRISTMAS_LIGHTS_ANIMATION_DURATION) / CHRISTMAS_LIGHTS_ANIMATION_DURATION;
    
    const colorCount = CHRISTMAS_COLORS.length;
    const currentColorIndex = Math.floor(progress * colorCount);
    const nextColorIndex = (currentColorIndex + 1) % colorCount;
    const localProgress = (progress * colorCount) % 1;
    
    this.lights.forEach((light, index) => {
      const offset = (index * 0.33) % 1;
      const adjustedProgress = (progress + offset) % 1;
      const adjustedColorIndex = Math.floor(adjustedProgress * colorCount);
      const adjustedNextColorIndex = (adjustedColorIndex + 1) % colorCount;
      const adjustedLocalProgress = (adjustedProgress * colorCount) % 1;
      
      const currentColor = CHRISTMAS_COLORS[adjustedColorIndex];
      const nextColor = CHRISTMAS_COLORS[adjustedNextColorIndex];
      
      const gradient = `radial-gradient(circle, ${currentColor} 0%, ${currentColor} 30%, transparent 70%)`;
      light.style.background = gradient;
    });
    
    this.animationId = requestAnimationFrame(() => this.updateGradients());
  }

  start() {
    if (this.isActive) return;
    
    this.isActive = true;
    this.startTime = Date.now();
    
    if (!this.container) {
      this.container = document.createElement('div');
      this.container.id = 'christmas-lights-container';
      this.container.style.position = 'fixed';
      this.container.style.top = '1000px';
      this.container.style.left = '0';
      this.container.style.width = '100%';
      this.container.style.height = '100%';
      this.container.style.pointerEvents = 'none';
      this.container.style.zIndex = CHRISTMAS_LIGHTS_Z_INDEX;
      this.container.style.transitionDuration = '1s';
      document.body.appendChild(this.container);
    }
    
    // Создаем три огонька
    this.lights = [
      this.createLight('left'),
      this.createLight('center'),
      this.createLight('right')
    ];
    
    this.lights.forEach(light => {
      this.container.appendChild(light);
    });
    
    this.updateGradients();
  }

  stop() {
    this.isActive = false;
    
    if (this.animationId) {
      cancelAnimationFrame(this.animationId);
      this.animationId = null;
    }
    
    if (this.container) {
      this.container.remove();
      this.container = null;
      this.lights = [];
    }
  }
}

const christmasLightsManager = new ChristmasLightsManager();

class TickerManager {
  constructor() {
    this.container = null;
    this.content = null;
    this.animationId = null;
    this.isActive = false;
    this.position = 0;
    this.speedMultiplier = 1;
  }

  start(lang = 'ru') {
    if (this.isActive) {
      this.updateLanguage(lang);
      return;
    }
    
    this.isActive = true;
    
    if (!this.container) {
      this.container = document.createElement('div');
      this.container.id = 'ticker-container';
      this.container.className = 'ticker-container';
      
      this.container.addEventListener('mouseenter', () => {
        this.speedMultiplier = TICKER_SPEED_HOVER / TICKER_SPEED;
      });
      
      this.container.addEventListener('mouseleave', () => {
        this.speedMultiplier = 1;
      });
      
      const appShell = document.querySelector('.app-shell');
      const footer = document.querySelector('.footer');
      if (appShell && footer) {
        appShell.insertBefore(this.container, footer);
      } else if (footer) {
        footer.parentNode.insertBefore(this.container, footer);
      } else {
        document.body.appendChild(this.container);
      }
    }

    this.updateLanguage(lang);
    this.animate();
  }

  updateLanguage(lang) {
    if (!this.container) return;
    
    const articles = ARTICLES[lang] || ARTICLES.ru;
    
    if (this.content) {
      this.content.remove();
    }
    
    this.content = document.createElement('div');
    this.content.className = 'ticker-content';
    
    articles.forEach((article, index) => {
      const link = document.createElement('a');
      link.href = article.url;
      link.target = '_blank';
      link.rel = 'noopener';
      link.textContent = article.title;
      link.style.color = 'inherit';
      link.style.textDecoration = 'none';
      link.style.transition = 'color 0.2s, text-decoration 0.2s';
      link.addEventListener('mouseenter', () => {
        link.style.color = 'var(--text)';
        link.style.textDecoration = 'underline';
      });
      link.addEventListener('mouseleave', () => {
        link.style.color = 'var(--muted)';
        link.style.textDecoration = 'none';
      });
      
      this.content.appendChild(link);
      if (index < articles.length - 1) {
        this.content.appendChild(document.createTextNode(' '));
      }
    });
    
    const duplicate = this.content.cloneNode(true);
    this.content.appendChild(document.createTextNode(' '));
    this.content.appendChild(duplicate);
    
    this.container.appendChild(this.content);
    this.position = 0;
  }

  animate() {
    if (!this.isActive) return;
    
    if (this.content) {
      this.position -= (TICKER_SPEED / 60) * this.speedMultiplier;
      const contentWidth = this.content.offsetWidth / 2;
      if (Math.abs(this.position) >= contentWidth) {
        this.position = 0;
      }
      this.content.style.transform = `translateX(${this.position}px)`;
    }
    
    this.animationId = requestAnimationFrame(() => this.animate());
  }

  stop() {
    this.isActive = false;
    
    if (this.animationId) {
      cancelAnimationFrame(this.animationId);
      this.animationId = null;
    }
    
    if (this.container) {
      this.container.remove();
      this.container = null;
      this.content = null;
    }
  }
}

const tickerManager = new TickerManager();

class ChristmasTreeButton {
  constructor() {
    this.element = null;
    this.isVisible = localStorage.getItem('christmas-tree-hidden') !== 'true';
  }

  create() {
    if (!this.isVisible || this.element) return;
    
    this.element = document.createElement('img');
    this.element.src = CHRISTMAS_TREE_IMAGE;
    this.element.className = 'christmas-tree-button';
    this.element.alt = 'Christmas tree';
    this.element.style.right = `-${CHRISTMAS_TREE_OFFSET_RIGHT}px`;
    this.element.style.width = `${CHRISTMAS_TREE_SIZE}px`;
    this.element.style.height = `${CHRISTMAS_TREE_SIZE}px`;
    this.element.style.zIndex = CHRISTMAS_TREE_Z_INDEX;
    
      this.element.addEventListener('click', () => {
        localStorage.setItem('christmas-tree-hidden', 'true');
        localStorage.setItem('ui-newyear', 'true');
        localStorage.setItem('newyear-mode-unlocked', 'true');
        this.element.remove();
        this.isVisible = false;
        location.reload();
      });
    
    document.body.appendChild(this.element);
  }
}

const christmasTreeButton = new ChristmasTreeButton();

const snowManagerBg = new SnowManager({
  sizeMin: SNOW_BG_SIZE_MIN,
  sizeMax: SNOW_BG_SIZE_MAX,
  speedMin: SNOW_BG_SPEED_MIN,
  speedMax: SNOW_BG_SPEED_MAX,
  count: SNOW_BG_COUNT,
  zIndex: SNOW_BG_Z_INDEX,
  opacity: SNOW_BG_OPACITY
});

const snowManagerFg = new SnowManager({
  sizeMin: SNOW_FG_SIZE_MIN,
  sizeMax: SNOW_FG_SIZE_MAX,
  speedMin: SNOW_FG_SPEED_MIN,
  speedMax: SNOW_FG_SPEED_MAX,
  count: SNOW_FG_COUNT,
  zIndex: SNOW_FG_Z_INDEX,
  opacity: SNOW_FG_OPACITY
});

(async () => {
  const template = await loadTemplate();

  createApp({
    template,
    data() {
      return {
        state: createInitialState(),
        stageAnimated: false,
        mathFunctions,
        themes,
        edgeThemes,
        i18n,
        icons: actionIcons,
        mobileMenuOpen: false,
        brandLogo: pickRandomLogo(),
        adCloseIcon,
        adBadgeIcon,
        adMuteIcon,
        burgerIcon: `<svg xmlns="http://www.w3.org/2000/svg" width="22" height="22" viewBox="0 0 448 512"><path fill="currentColor" d="M0 96c0-17.7 14.3-32 32-32h384c17.7 0 32 14.3 32 32s-14.3 32-32 32H32c-17.7 0-32-14.3-32-32m0 160c0-17.7 14.3-32 32-32h384c17.7 0 32 14.3 32 32s-14.3 32-32 32H32c-17.7 0-32-14.3-32-32m448 160c0-17.7 14.3 32-32 32H32c-17.7 0-32-14.3-32-32s14.3-32 32-32h384c17.7 0 32 14.3 32 32"/></svg>`,
        burgerMenuMaskId: `lineMdCompass${Date.now()}-${Math.random().toString(36).substr(2, 9)}`,
      };
    },
    computed: {
      burgerIconOpen() {
        const maskId = this.burgerMenuMaskId;
        return `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Compass SVG Icon</title><mask id="${maskId}"><path fill="none" stroke="#fff" stroke-dasharray="60" stroke-dashoffset="60" stroke-linecap="round" stroke-width="2" d="M12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="60;0"/></path><path fill="#fff" d="M11 11L12 12L13 13L12 12z"><set attributeName="opacity" begin="0.6s" to="1"/><animate fill="freeze" attributeName="d" begin="0.6s" dur="0.3s" values="M11 11L12 12L13 13L12 12z;M10.2 10.2L17 7L13.8 13.8L7 17z"/><animateTransform attributeName="transform" begin="0.5s" dur="0.5s" type="rotate" values="-180 12 12;0 12 12"/></path><circle cx="12" cy="12" r="1" fill-opacity="0"><animate fill="freeze" attributeName="fill-opacity" begin="0.8s" dur="0.3s" values="0;1"/></circle></mask><rect width="24" height="24" fill="currentColor" mask="url(#${maskId})"/></svg>`;
      },
      newYearModeUnlocked() {
        return localStorage.getItem('newyear-mode-unlocked') === 'true';
      },
    },
    mounted() {
      this.initFromStorage();
      this.runStageAnimation();
      this.spawnAds();
      this.updateTitle();
      this.initSnow();
      this.initTicker();
      christmasTreeButton.create();
    },
    methods: {
      getRandomLoadingText() {
        const lang = this.state.ui.lang || 'ru';
        const texts = LOADING_TEXTS[lang] || LOADING_TEXTS.ru;
        return texts[Math.floor(Math.random() * texts.length)];
      },
      async showLoadingWithMinTime(callback) {
        const startTime = Date.now();
        this.state.loadingText = this.getRandomLoadingText();
        this.state.loadingGlobal = true;
        try {
          await callback();
        } finally {
          const elapsed = Date.now() - startTime;
          const remaining = Math.max(0, MIN_LOADING_TIME - elapsed);
          await new Promise(resolve => setTimeout(resolve, remaining));
          this.state.loadingGlobal = false;
          this.$nextTick(() => {
            this.runStageAnimation();
          });
        }
      },
      resetState() {
        const fresh = createInitialState();
        Object.keys(fresh).forEach((key) => {
          this.state[key] = fresh[key];
        });
        this.stageAnimated = false;
        this.spawnAds();
      },
      toast(message, type = 'success', showDetail = false) {
        const id = Date.now();
        const lang = this.state.ui.lang || 'ru';
        const titles = {
          success: this.t('successLabel'),
          error: this.t('error'),
          warning: this.t('warningLabel'),
          info: this.t('infoLabel'),
        };
        const detailsRu = {
          success: 'Операция выполнена успешно, данные сохранены.',
          warning: 'Проверьте введённые данные и попробуйте снова.',
          info: 'Это информационное уведомление, действий не требуется.',
          error: 'Произошла ошибка, проверьте детали и повторите попытку.',
        };
        const detailsEn = {
          success: 'Operation completed successfully, data has been saved.',
          warning: 'Please verify your input and try again.',
          info: 'This is an informational notification, no action is required.',
          error: 'An error occurred, review the details and try again.',
        };
        let msg = message || '';
        if (showDetail || !message) {
          const detail = (lang === 'en' ? detailsEn[type] : detailsRu[type]) || (lang === 'en' ? detailsEn.info : detailsRu.info);
          msg = message
            ? `${message}${/[.!?]$/.test(message.trim()) ? '' : '.'} ${detail}`
            : detail;
        } else if (message && !/[.!?]$/.test(message.trim())) {
          msg = message + '.';
        }
        const icons = {
          success: 'https://media.tenor.com/bvXwJ4I19ZQAAAAi/cat-cat-meme.gif',
          warning: 'https://media1.tenor.com/m/A4XEGPtSd-4AAAAd/rowe-cat.gif',
          error: 'https://media.tenor.com/cXe--DrS0iYAAAAi/warthog.gif',
          info: 'https://media1.tenor.com/m/pwJ6OF2elfYAAAAd/stupid-cat-cat-stare.gif',
        };
        this.state.toasts.push({
          id,
          message: msg,
          type,
          title: titles[type] || titles.info,
          icon: icons[type] || icons.info,
          leaving: false,
        });
        setTimeout(() => {
          this.state.toasts = this.state.toasts.map((t) => (t.id === id ? { ...t, leaving: true } : t));
        }, 2600);
        setTimeout(() => {
          this.state.toasts = this.state.toasts.filter((t) => t.id !== id);
        }, 3200);
      },
      spawnAds() {
        if (this.state.ads.adBlock) {
          this.state.ads.slots.functions = null;
          this.state.ads.slots.graph = null;
          return;
        }
        const [fnAd, graphAd] = pickRandomAds(2);
        // Левый блок рекламы появляется с шансом ниже
        const shouldShowFunctionsAd = Math.random() < 1.0;
        this.state.ads.slots.functions = shouldShowFunctionsAd ? (fnAd || null) : null;
        // Правый блок всегда
        this.state.ads.slots.graph = graphAd || fnAd || null;
        this.state.ads.dismissed.functions = false;
        this.state.ads.dismissed.graph = false;
      },
      closeAd(slot) {
        if (!slot) return;
        this.state.ads.dismissed = {
          ...this.state.ads.dismissed,
          [slot]: true,
        };
      },
      showError(title, message) {
        this.state.modals.error = { title, message };
      },
      async initFromStorage() {
        const saved = readCredentials();
        if (saved?.username && saved?.password) {
          this.state.credentials.username = saved.username;
          this.state.credentials.password = saved.password;
          this.state.credentials.email = saved.email || '';
          this.state.credentials.userId = saved.userId ?? null;
          api.setAuth(saved.username, saved.password);
          this.state.isAuthed = true;
          await this.loadCurrentUser();
          this.refreshFunctions().catch(() => {
            this.state.isAuthed = false;
          });
        }
      },
      async loadCurrentUser() {
        try {
          const me = await api.get('/users/me');
          if (me?.userId) this.state.credentials.userId = me.userId;
          if (me?.username) this.state.credentials.username = me.username;
          if (me?.email) this.state.credentials.email = me.email;
          persistCredentials(this.state.credentials);
          return me;
        } catch (e) {
          return null;
        }
      },
      async register() {
        try {
          if (!this.state.authForm.username || !this.state.authForm.password) {
            throw new Error(this.t('errorFillLoginPassword'));
          }
          if (this.state.authForm.username.length > 32) {
            throw new Error(this.t('errorUsernameTooLong'));
          }
          if (this.state.authForm.email && this.state.authForm.email.length > 128) {
            throw new Error(this.t('errorEmailTooLong'));
          }
          if (this.state.authForm.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.state.authForm.email)) {
            throw new Error(this.t('errorEmailInvalid'));
          }
          if (this.state.authForm.password.length > 128) {
            throw new Error(this.t('errorPasswordTooLong'));
          }
          if (this.state.authForm.password !== this.state.authForm.confirm) {
            throw new Error(this.t('errorPasswordsNotMatch'));
          }
          this.state.loading = true;
          const res = await api.post('/auth/register', {
            username: this.state.authForm.username,
            password: this.state.authForm.password,
            email: this.state.authForm.email,
          });
          this.toast(this.t('toastRegisterSuccess'));
          this.state.authMode = 'login';
          this.state.credentials.username = this.state.authForm.username;
          this.state.credentials.password = this.state.authForm.password;
          this.state.credentials.email = this.state.authForm.email;
          if (res?.userId) {
            this.state.credentials.userId = res.userId;
          }
        } catch (e) {
          const msg = e.message || this.t('errorRegistrationFailed');
          this.showError(this.t('register'), msg);
        } finally {
          this.state.loading = false;
        }
      },
      async tryLogin() {
        try {
          const { username, password } = this.state.credentials;
          if (!username || !password) throw new Error(this.t('errorEnterLoginPassword'));
          this.state.loading = true;
          api.setAuth(username, password);
          await this.loadCurrentUser();
          await api.get('/functions');
          this.state.isAuthed = true;
          persistCredentials(this.state.credentials);
          await this.refreshFunctions();
          this.toast(this.t('toastConnectionEstablished'));
        } catch (e) {
          api.clearAuth();
          let msg = e.message || this.t('errorLoginFailed');
          if (e.message?.includes('Unauthorized')) {
            msg = this.t('errorWrongLoginPassword');
          }
          this.showError(this.t('login'), msg);
        } finally {
          this.state.loading = false;
        }
      },
      logout() {
        api.clearAuth();
        localStorage.removeItem(STORAGE_KEY);
        localStorage.removeItem('christmas-tree-hidden');
        localStorage.removeItem('newyear-mode-unlocked');
        localStorage.setItem('ui-newyear', 'false');
        snowManagerBg.stop();
        snowManagerFg.stop();
        christmasLightsManager.stop();
        chart.destroy();
        this.resetState();
        if (christmasTreeButton.element) {
          christmasTreeButton.element.remove();
          christmasTreeButton.element = null;
        }
        christmasTreeButton.isVisible = true;
        this.$nextTick(() => {
          christmasTreeButton.create();
        });
      },
      applyTheme() {
        document.documentElement.setAttribute('data-theme', this.state.ui.theme);
        document.documentElement.setAttribute('data-inclusive', this.state.ui.inclusive ? 'true' : 'false');
        document.documentElement.setAttribute('data-edges', this.state.ui.edges || 'soft');
      },
      scheduleApplyTheme() {
        if (this._applyRaf) cancelAnimationFrame(this._applyRaf);
        this._applyRaf = requestAnimationFrame(() => {
          this.applyTheme();
          this._applyRaf = null;
        });
      },
      t(key) {
        return (i18n[this.state.ui.lang] && i18n[this.state.ui.lang][key]) || key;
      },
      setFavicon(iconPath) {
        let link = document.querySelector('#favicon');
        if (!link) {
          link = document.createElement('link');
          link.id = 'favicon';
          link.rel = 'icon';
          link.type = 'image/svg+xml';
          document.head.appendChild(link);
        }
        link.href = iconPath;
      },
      updateTitle() {
        const lang = this.state.ui.lang || 'ru';
        let title = this.t('pageTitle');
        let favicon = faviconIcons.default;
        
        if (!this.state.isAuthed) {
          title = this.t('pageTitleAuth');
          favicon = faviconIcons.auth;
        } else if (this.state.modals.profile) {
          title = this.t('pageTitleProfile');
          favicon = faviconIcons.profile;
        } else if (this.state.modals.settings) {
          title = this.t('pageTitleSettings');
          favicon = faviconIcons.settings;
        } else if (this.state.modals.create || this.state.modals.math || this.state.modals.composite) {
          title = this.t('pageTitleNewFunction');
          favicon = faviconIcons.newFunction;
        }
        
        document.title = title;
        this.setFavicon(favicon);
      },
      typeLabel(key) {
        return (i18n[this.state.ui.lang]?.types && i18n[this.state.ui.lang].types[key]) || key;
      },
      themeLabel(key) {
        return (i18n[this.state.ui.lang]?.themes && i18n[this.state.ui.lang].themes[key]) || key;
      },
      edgeLabel(key) {
        return (i18n[this.state.ui.lang]?.edgesDict && i18n[this.state.ui.lang].edgesDict[key]) || key;
      },
      themeIcon(key) {
        return themeIcons[key] || themeIcons.default;
      },
      edgeIcon(key) {
        return edgeIcons[key] || edgeIcons.soft;
      },
      isCompositeSelected() {
        const t = (this.state.selectedFunction?.functionType || '').toUpperCase();
        return this.state.selectedFunction?._composite || t === 'COMPOSITE';
      },
      toggleMobileMenu() {
        this.mobileMenuOpen = !this.mobileMenuOpen;
      },
      closeMobileMenu() {
        this.mobileMenuOpen = false;
      },
      async refreshFunctions() {
        await this.showLoadingWithMinTime(async () => {
          try {
            const [fnData, compData] = await Promise.all([
              api.get('/functions'),
              api.get('/composite-functions').catch(() => []),
            ]);
            const baseFunctions = (fnData || []).map((f) => normalizeFunction(f));
            const composites = (compData || []).map((c) => {
              const id = c.compositeId ?? c.id;
              const firstId = c.firstFunctionId ?? c.first_function_id;
              const secondId = c.secondFunctionId ?? c.second_function_id;
              console.log(`[refreshFunctions] Loading composite ${id}: firstFunctionId=${firstId}, secondFunctionId=${secondId}`, c);
              return normalizeFunction({
                ...c,
                compositeId: id,
                functionId: `comp-${id}`,
                functionName: c.compositeName ?? c.name ?? 'Composite',
                functionType: 'COMPOSITE',
                firstFunctionId: firstId,
                secondFunctionId: secondId,
                xFrom: c.xFrom ?? c.x_from ?? null,
                xTo: c.xTo ?? c.x_to ?? null,
                _composite: true,
              });
            });
            this.state.functions = [...baseFunctions, ...composites];
          if (!this.state.credentials.userId && fnData && fnData.length > 0) {
            const firstId = fnData.map((f) => f.userId).find((v) => v);
            if (firstId) {
              this.state.credentials.userId = firstId;
              persistCredentials(this.state.credentials);
            }
          }
            if (this.state.selectedFunction) {
              const stillExists = this.state.functions.find((f) => f.functionId === this.state.selectedFunction.functionId);
              if (!stillExists) {
                this.state.selectedFunction = null;
                this.state.points = [];
                chart.destroy();
              } else {
                await this.loadPoints(this.state.selectedFunction.functionId);
              }
            }
          } catch (e) {
            const onlyBase = await api.get('/functions').catch(() => []);
            this.state.functions = onlyBase || [];
            this.showError(this.t('errorLoadingFunctions'), e.message);
          }
        });
      },
      async deleteFunction(id) {
        try {
          const functionToDelete = this.state.functions.find(f => f.functionId === id);
          const isComposite = functionToDelete?._composite || (functionToDelete?.functionType || '').toUpperCase() === 'COMPOSITE';
          
          if (isComposite) {
            const compositeId = functionToDelete.compositeId ?? functionToDelete.composite_id ?? id;
            await api.delete(`/composite-functions/${compositeId}`);
          } else {
            await api.delete(`/functions/${id}`);
          }
          
          this.toast(this.t('toastFunctionDeleted'), 'warning', false);
          await this.refreshFunctions();
          if (this.state.selectedFunction?.functionId === id) {
            this.state.selectedFunction = null;
            this.state.points = [];
            chart.destroy();
          }
        } catch (e) {
          this.showError(this.t('errorDeletingFunction'), e.message);
        }
      },
      async loadPoints(functionId) {
        try {
          const selectedRaw = this.state.functions.find((f) => String(f.functionId) === String(functionId)) || null;
          this.state.selectedFunction = selectedRaw ? normalizeFunction(selectedRaw) : null;
          this.state.graph.evalX = null;
          let points = [];

          const isComposite = this.state.selectedFunction?._composite || (this.state.selectedFunction?.functionType || '').toUpperCase() === 'COMPOSITE';

          if (isComposite) {
            try {
              let compId = this.state.selectedFunction.compositeId ?? this.state.selectedFunction.composite_id;
              if (!compId && typeof functionId === 'string' && functionId.startsWith('comp-')) {
                compId = Number(functionId.replace('comp-', ''));
              }
              let firstId = this.state.selectedFunction.firstFunctionId;
              let secondId = this.state.selectedFunction.secondFunctionId;
              console.log(`[loadPoints] Initial IDs from selectedFunction: firstId=${firstId}, secondId=${secondId}`);
              console.log(`[loadPoints] selectedFunction:`, {
                functionId: this.state.selectedFunction.functionId,
                functionName: this.state.selectedFunction.functionName,
                functionType: this.state.selectedFunction.functionType,
                firstFunctionId: this.state.selectedFunction.firstFunctionId,
                secondFunctionId: this.state.selectedFunction.secondFunctionId,
                compositeId: this.state.selectedFunction.compositeId
              });
              if (!firstId || !secondId) {
                try {
                  const compMeta = await api.get(`/composite-functions/${compId || functionId}/points`);
                  console.log(`[loadPoints] Fetched compMeta:`, compMeta);
                  firstId = firstId || compMeta?.firstFunctionId || compMeta?.first_function_id;
                  secondId = secondId || compMeta?.secondFunctionId || compMeta?.second_function_id;
                } catch (err) {
                  console.warn('composite meta fetch failed', err);
                }
              }
              if (!firstId || !secondId) {
                this.toast(this.t('toastCompositeChildError'), 'warning');
                chart.destroy();
                this.state.points = [];
                return;
              }
              console.log(`[loadPoints] About to compute composite: firstId=${firstId}, secondId=${secondId}`);
              if (firstId === secondId) {
                console.warn(`[loadPoints] WARNING: firstId === secondId (${firstId})! This means the same function is used for both parts of the composition.`);
              }
              points = await composeFunctions(firstId, secondId);
              console.log(`[loadPoints] Composite function points computed: ${points.length} points`);
              if (points.length > 0) {
                console.log(`[loadPoints] First 3 computed points:`, points.slice(0, 3).map(p => `(${p.xValue.toFixed(3)}, ${p.yValue.toFixed(3)})`));
                console.log(`[loadPoints] Last 3 computed points:`, points.slice(-3).map(p => `(${p.xValue.toFixed(3)}, ${p.yValue.toFixed(3)})`));
              }
              if (!points.length) {
                this.toast(this.t('toastCompositeOutOfRange'), 'warning');
                chart.destroy();
                this.state.points = [];
                return;
              }
            } catch (err) {
              this.toast(this.t('toastCompositeCalcError'), 'warning');
              console.error(err);
              chart.destroy();
              this.state.points = [];
              return;
            }
          } else {
            const raw = await api.get(`/functions/${functionId}/points`);
            console.log('points raw', raw);
            points = (raw || [])
              .map(normalizePoint)
              .map((p, idx) => ({
                ...p,
                xValue: p.xValue,
                yValue: p.yValue,
                pointId: p.pointId ?? p.id ?? `local-${idx}`,
                functionId: p.functionId ?? functionId,
              }))
              .sort((a, b) => {
                const ax = Number(a.xValue);
                const bx = Number(b.xValue);
                if (Number.isFinite(ax) && Number.isFinite(bx)) return ax - bx;
                return 0;
              });

            if (!points.length && this.state.selectedFunction) {
              const fType = (this.state.selectedFunction.functionType || '').toUpperCase();
              if (fType === 'TABULATED' || fType === 'COMPOSITE') {
                this.toast(this.t('toastNoPoints'), 'warning');
              } else {
                points = synthesizePointsFromFunction(this.state.selectedFunction);
                if (!points.length) {
                  this.toast(this.t('toastNoPoints'), 'warning');
                }
              }
            }
          }

          this.state.points = points;
          this.state.graph.evalResult = null;
          chart.render(this.state.points, (xVal) => {
            this.state.graph.evalX = xVal;
            this.evalFunction();
          });
        } catch (e) {
          this.showError(this.t('errorFunctionPoints'), e.message);
        }
      },
      async updatePoint(point) {
        try {
          const isComposite = this.state.selectedFunction?._composite || (this.state.selectedFunction?.functionType || '').toUpperCase() === 'COMPOSITE';
          if (isComposite) {
            this.toast(this.t('toastCompositePointsChange'), 'warning');
            return;
          }
          const isPersisted = Number.isFinite(Number(point.pointId));
          const x = Number(point.xValue);
          const y = Number(point.yValue);
          if (!Number.isFinite(x) || !Number.isFinite(y)) {
            throw new Error('Введите числовые x и y');
          }
          if (isPersisted) {
            await api.put(`/points/${point.pointId}`, {
              xValue: x,
              yValue: y,
            });
            this.toast(this.t('toastPointUpdated'));
          } else if (this.state.selectedFunction?.functionId) {
            await api.post(`/functions/${this.state.selectedFunction.functionId}/points`, {
              xValue: x,
              yValue: y,
            });
            this.toast(this.t('toastPointSaved'));с          } else {
            this.toast(this.t('toastNoFunctionSelected'), 'warning');
            return;
          }
          await this.loadPoints(point.functionId || this.state.selectedFunction?.functionId);
        } catch (e) {
          this.showError(this.t('errorUpdatingPoint'), e.message);
        }
      },
      async deletePoint(point) {
        try {
          const isComposite = this.state.selectedFunction?._composite || (this.state.selectedFunction?.functionType || '').toUpperCase() === 'COMPOSITE';
          if (isComposite) {
            this.toast(this.t('toastDeleteInBaseFunctions'), 'warning');
            return;
          }
          const isPersisted = Number.isFinite(Number(point.pointId));
          if (isPersisted) {
            await api.delete(`/points/${point.pointId}`);
            this.toast(this.t('toastPointDeleted'), 'warning', false);
          } else {
            this.state.points = this.state.points.filter((p) => p.pointId !== point.pointId);
            this.toast(this.t('toastLocalPointDeleted'), 'warning', false);
          }
          await this.loadPoints(point.functionId || this.state.selectedFunction?.functionId);
        } catch (e) {
          this.showError(this.t('errorDeletingPoint'), e.message);
        }
      },
      addPointRow() {
        this.state.createForm.points.push({ x: 0, y: 0 });
      },
      removePointRow(idx) {
        this.state.createForm.points.splice(idx, 1);
      },
      async createFunctionWithPoints({ name, type, xFrom, xTo, points }) {
        if (!this.state.credentials.userId && this.state.functions.length) {
          this.state.credentials.userId = this.state.functions[0].userId ?? null;
        }
        if (!this.state.credentials.userId) {
          throw new Error('Не удалось определить userId. Перелогиньтесь.');
        }
        const safeName = ensureName(name || 'f(x)');
        const safeType = ensureType(type || 'TABULATED');
        const safeXFrom = ensureOptionalNumber(xFrom, 'xFrom');
        const safeXTo = ensureOptionalNumber(xTo, 'xTo');
        const safePoints = sanitizePoints(points);

        const payload = {
          userId: Number(this.state.credentials.userId),
          functionName: safeName,
          functionType: safeType,
          xFrom: safeXFrom,
          xTo: safeXTo,
        };

        const fn = await api.post('/functions', payload);
        for (const p of safePoints) {
          await api.post(`/functions/${fn.functionId}/points`, {
            xValue: p.xValue,
            yValue: p.yValue,
          });
        }
        this.state.points = safePoints.map((p, idx) => ({
          pointId: `local-${idx}`,
          functionId: fn.functionId,
          xValue: p.xValue,
          yValue: p.yValue,
        }));
        return fn;
      },
      async createFromTable() {
        try {
          const name = ensureName(this.state.createForm.name || 'f(x)');
          const type = ensureType(this.state.createForm.type || 'TABULATED');
          const xFrom = ensureOptionalNumber(this.state.createForm.xFrom, 'xFrom');
          const xTo = ensureOptionalNumber(this.state.createForm.xTo, 'xTo');
          const points = sanitizePoints(this.state.createForm.points);
          const fn = await this.createFunctionWithPoints({
            name,
            type,
            xFrom,
            xTo,
            points,
          });
          this.toast(this.t('toastFunctionSaved'));
          this.state.modals.create = false;
          await this.refreshFunctions();
          await this.loadPoints(fn.functionId);
          this.state.createForm = createInitialState().createForm;
        } catch (e) {
          this.showError(this.t('errorCreatingFunction'), e.message);
        }
      },
      async createFromMath() {
        try {
          const { mathKey } = this.state.mathForm;
          const xFrom = ensureSafeNumber(this.state.mathForm.xFrom, 'xFrom');
          const xTo = ensureSafeNumber(this.state.mathForm.xTo, 'xTo');
          const pointsCount = ensurePointsCount(this.state.mathForm.pointsCount);
          const constant = ensureOptionalNumber(this.state.mathForm.constant, 'const');
          const label = (mathFunctions.find((m) => m.key === mathKey)?.label) || 'f(x)';
          const finalName = ensureName(this.state.mathForm.name || label, label);
          const step = (xTo - xFrom) / (pointsCount - 1);
          const evalFn = mathEvaluators[mathKey];
          const pointsRaw = Array.from({ length: pointsCount }, (_, idx) => {
            const x = xFrom + idx * step;
            const y = evalFn ? evalFn(x, constant) : 0;
            return { xValue: x, yValue: y };
          });
          const exceeds = pointsRaw.find((p) => Math.abs(p.yValue) > LIMITS.NUMBER_ABS_MAX);
          if (exceeds) {
            throw new Error(`Рассчитанные y превышают лимит ±${LIMITS.NUMBER_ABS_MAX}. Уменьшите диапазон или выберите другую функцию.`);
          }
          const points = sanitizePoints(pointsRaw);
          const fn = await this.createFunctionWithPoints({
            name: finalName,
            type: 'MATH',
            xFrom,
            xTo,
            points,
          });
          this.toast(this.t('toastFunctionFromFormula'));
          this.state.modals.math = false;
          await this.refreshFunctions();
          await this.loadPoints(fn.functionId);
          this.state.mathForm = createInitialState().mathForm;
        } catch (e) {
          this.showError(this.t('errorCreatingFromFormula'), e.message);
        }
      },
      async saveComposite() {
        try {
          if (!this.state.credentials.userId) throw new Error('Укажите userId');
          const { a, b } = this.state.compositeForm;
          const name = ensureName(this.state.compositeForm.name, 'composite');
          if (!name || !a || !b) throw new Error('Заполните имя и выберите обе функции');
          console.log(`[saveComposite] Saving composite: name=${name}, firstFunctionId=${a}, secondFunctionId=${b}`);
          if (a === b) {
            console.warn(`[saveComposite] WARNING: firstFunctionId === secondFunctionId (${a})! This will create a composition of a function with itself.`);
          }
          await api.post('/composite-functions', {
            userId: Number(this.state.credentials.userId),
            compositeName: name,
            firstFunctionId: a,
            secondFunctionId: b,
          });
          this.toast(this.t('toastCompositeSaved'));
          this.state.compositeForm = createInitialState().compositeForm;
          this.state.modals.composite = false;
          await this.refreshFunctions();
        } catch (e) {
          this.showError(this.t('errorCompositeFunction'), e.message);
        }
      },
      addEmptyPointRow() {
        const isComposite = this.state.selectedFunction?._composite || (this.state.selectedFunction?.functionType || '').toUpperCase() === 'COMPOSITE';
        if (isComposite) {
          this.toast(this.t('toastAddPointsInChild'), 'warning');
          return;
        }
        this.state.points.push({
          pointId: `tmp-${Date.now()}`,
          functionId: this.state.selectedFunction?.functionId,
          xValue: 0,
          yValue: 0,
        });
      },
      openSettings() {
        this.state.modals.settings = true;
      },
      saveSettings() {
        localStorage.setItem('ui-theme', this.state.ui.theme);
        localStorage.setItem('ui-lang', this.state.ui.lang);
        localStorage.setItem('ui-inclusive', this.state.ui.inclusive ? 'true' : 'false');
        localStorage.setItem('ui-edges', this.state.ui.edges || 'soft');
        localStorage.setItem('ui-adblock', this.state.ads.adBlock ? 'true' : 'false');
        localStorage.setItem('ui-newyear', this.state.ui.newYearMode ? 'true' : 'false');
        localStorage.setItem('ui-ticker', this.state.ui.tickerEnabled ? 'true' : 'false');
        this.scheduleApplyTheme();
        this.state.modals.settings = false;
        this.toast(this.t('toastSettingsSaved'));
        location.reload();
      },
  async openProfile() {
    await this.showLoadingWithMinTime(async () => {
      try {
        const me = await this.loadCurrentUser(this.state.credentials.username);
        this.state.profileForm.username = me?.username || this.state.credentials.username || '';
        this.state.profileForm.email = me?.email || this.state.credentials.email || this.state.authForm.email || '';
        this.state.profileForm.password = '';
        this.state.modals.profile = true;
      } catch (e) {
        this.showError(this.t('profile'), e.message || this.t('errorLoadingProfile'));
      }
    });
  },
  async saveProfile() {
    try {
      if (!this.state.credentials.userId) {
            await this.loadCurrentUser();
          }
          if (!this.state.credentials.userId) {
            this.showError(this.t('profile'), this.t('errorUserIdUnknown'));
            return;
      }
      if (!/^[A-Za-z0-9_-]{3,32}$/.test(this.state.profileForm.username)) {
        throw new Error(this.t('errorUsernameFormat'));
      }
      if (this.state.profileForm.email && !/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(this.state.profileForm.email)) {
        throw new Error(this.t('errorEmailInvalid'));
      }
      if (this.state.profileForm.password && this.state.profileForm.password.length < 6) {
        throw new Error(this.t('errorPasswordTooShort'));
      }
      if (this.state.profileForm.password) {
        const pwd = this.state.profileForm.password;
        const strong = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{6,128}$/;
        if (!strong.test(pwd)) {
          throw new Error(this.t('errorPasswordWeak'));
        }
      }
      const payload = {
        username: this.state.profileForm.username,
        email: this.state.profileForm.email,
        password: this.state.profileForm.password || undefined,
      };
      const trySave = async () => {
        await api.put(`/users/${this.state.credentials.userId}`, payload);
      };
      try {
        await trySave();
      } catch (err) {
        if (String(err.message || '').includes('404')) {
          await this.loadCurrentUser();
          if (!this.state.credentials.userId) throw err;
          await trySave();
        } else {
          throw err;
        }
      }
      this.toast(this.t('update'));
      this.state.credentials.username = this.state.profileForm.username;
      this.state.credentials.email = this.state.profileForm.email;
      if (this.state.profileForm.password) {
        this.state.credentials.password = this.state.profileForm.password;
        api.setAuth(this.state.credentials.username, this.state.credentials.password);
      }
      persistCredentials(this.state.credentials);
      this.state.modals.profile = false;
    } catch (e) {
      const low = (e.message || '').toLowerCase();
      let msg;
      if (low.includes('already exists') || low.includes('username') && low.includes('exists')) {
        msg = this.t('errorUsernameTaken');
      } else {
        msg = e.message || this.t('error');
      }
      this.showError(this.t('profile'), msg);
    }
  },
  async deleteAccount() {
    try {
          if (!this.state.credentials.userId) await this.loadCurrentUser();
          if (!this.state.credentials.userId) throw new Error(this.state.ui.lang === 'en'
            ? 'User id is unknown. Please re-login.'
            : 'UserId не определён. Перелогиньтесь.');
      await api.delete(`/users/${this.state.credentials.userId}`);
      this.toast(this.t('deleteAccount'), 'warning');
      this.logout();
    } catch (e) {
      const msg = e.message?.includes('403')
        ? (this.state.ui.lang === 'en' ? 'You have no rights to delete this user.' : 'Нет прав удалить этого пользователя.')
        : (e.message || (this.state.ui.lang === 'en' ? 'Failed to delete user' : 'Не удалось удалить пользователя'));
      this.showError(this.t('errorDeleting'), msg);
    }
  },
      async saveAllPoints() {
        if (!this.state.selectedFunction || !this.state.points.length) return;
        const isComposite = this.state.selectedFunction?._composite || (this.state.selectedFunction?.functionType || '').toUpperCase() === 'COMPOSITE';
        if (isComposite) {
          this.toast(this.t('toastSaveInBaseFunctions'), 'warning');
          return;
        }
        try {
          const fnId = this.state.selectedFunction.functionId;
          this.state.points = this.state.points.map((p, idx) => ({
            ...p,
            xValue: ensureSafeNumber(p.xValue, `x[${idx + 1}]`),
            yValue: ensureSafeNumber(p.yValue, `y[${idx + 1}]`),
          }));
          for (const p of this.state.points) {
            const isPersisted = Number.isFinite(Number(p.pointId));
            if (isPersisted) {
              await api.put(`/points/${p.pointId}`, {
                xValue: Number(p.xValue),
                yValue: Number(p.yValue),
              });
            } else {
              await api.post(`/functions/${fnId}/points`, {
                xValue: Number(p.xValue),
                yValue: Number(p.yValue),
              });
            }
          }
          this.toast(this.t('save'));
          await this.loadPoints(fnId);
        } catch (e) {
          this.showError(this.t('pointsTitle'), e.message);
        }
      },
      runStageAnimation() {
        this.stageAnimated = true;
      },
      exportJson() {
        if (!this.state.selectedFunction) return;
        const payload = {
          function: this.state.selectedFunction,
          points: this.state.points,
        };
        const blob = new Blob([JSON.stringify(payload, null, 2)], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `${this.state.selectedFunction.functionName || 'function'}.json`;
        a.click();
        URL.revokeObjectURL(url);
        this.toast(this.t('toastExportedJson'));
      },
      async importJson(event) {
        const file = event.target.files?.[0];
        if (!file) return;
        try {
          const text = await file.text();
          const data = JSON.parse(text);
          const points = Array.isArray(data)
            ? data
            : data.points || [];
          if (!points.length) throw new Error('Файл не содержит точек');
          this.state.createForm.points = points.map((p) => ({
            x: p.xValue ?? p.x,
            y: p.yValue ?? p.y,
          }));
          this.state.modals.create = true;
          this.toast(this.t('toastPointsLoaded'));
        } catch (e) {
          this.showError(this.t('errorImport'), e.message);
        } finally {
          event.target.value = '';
        }
      },
      evalFunction() {
        const x = Number(this.state.graph.evalX);
        if (!this.state.points.length || Number.isNaN(x)) {
          this.state.graph.evalResult = null;
          return;
        }
        this.state.graph.evalResult = linearInterpolate(this.state.points, x);
      },
      initSnow() {
        if (this.state.ui.newYearMode) {
          snowManagerBg.start();
          snowManagerFg.start();
          christmasLightsManager.start();
        } else {
          snowManagerBg.stop();
          snowManagerFg.stop();
          christmasLightsManager.stop();
        }
      },
      initTicker() {
        if (this.state.ui.tickerEnabled) {
          tickerManager.start(this.state.ui.lang);
        } else {
          tickerManager.stop();
        }
      },
    },
    watch: {
      'state.ui.theme'(val, old) {
        if (val === old) return;
        localStorage.setItem('ui-theme', this.state.ui.theme);
        this.scheduleApplyTheme();
      },
      'state.ui.inclusive'(val, old) {
        if (val === old) return;
        localStorage.setItem('ui-inclusive', this.state.ui.inclusive ? 'true' : 'false');
        this.scheduleApplyTheme();
        location.reload();
      },
      'state.ui.edges'(val, old) {
        if (val === old) return;
        localStorage.setItem('ui-edges', this.state.ui.edges || 'soft');
        this.scheduleApplyTheme();
      },
      'state.ui.lang'() {
        localStorage.setItem('ui-lang', this.state.ui.lang);
        this.updateTitle();
        if (this.state.ui.tickerEnabled) {
          tickerManager.updateLanguage(this.state.ui.lang);
        }
      },
      'state.ui.tickerEnabled'(val, old) {
        if (val === old) return;
        localStorage.setItem('ui-ticker', val ? 'true' : 'false');
        if (val) {
          tickerManager.start(this.state.ui.lang);
        } else {
          tickerManager.stop();
        }
      },
      'state.isAuthed'() {
        this.updateTitle();
      },
      'state.modals.profile'() {
        this.updateTitle();
      },
      'state.modals.settings'() {
        this.updateTitle();
      },
      'state.modals.create'() {
        this.updateTitle();
      },
      'state.modals.math'() {
        this.updateTitle();
      },
      'state.modals.composite'() {
        this.updateTitle();
      },
      'state.authMode'() {
        if (!this.state.isAuthed) {
          this.updateTitle();
        }
      },
      'state.ads.adBlock'(val, old) {
        if (val === old) return;
        localStorage.setItem('ui-adblock', val ? 'true' : 'false');
        if (val) {
          this.state.ads.slots.functions = null;
          this.state.ads.slots.graph = null;
        } else {
          this.spawnAds();
        }
      },
      'state.ui.newYearMode'(val, old) {
        if (val === old) return;
        localStorage.setItem('ui-newyear', val ? 'true' : 'false');
        this.initSnow();
      },
    },
    created() {
      this.applyTheme();
    },
  }).mount('#app');
})();

