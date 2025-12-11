import { createApp } from 'https://unpkg.com/vue@3/dist/vue.esm-browser.prod.js';
import { Api } from './api.js';
import { ChartManager } from './chart.js';
import { createInitialState, mathEvaluators, mathFunctions } from './state.js';

const api = new Api('/api');
const chart = new ChartManager('fn-chart');
const STORAGE_KEY = 'labs-oop-ui';
const themes = ['dark','light','neo','sunset','ocean','forest','candy','mono','cyber'];
const edgeThemes = ['round','soft','rough'];
const themeIcons = {
  light: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Sunny-outline-loop SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2"><path stroke-dasharray="34" stroke-dashoffset="34" d="M12 7C14.76 7 17 9.24 17 12C17 14.76 14.76 17 12 17C9.24 17 7 14.76 7 12C7 9.24 9.24 7 12 7"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.4s" values="34;0"/></path><g stroke-dasharray="2" stroke-dashoffset="2"><path d="M0 0"><animate fill="freeze" attributeName="d" begin="0.5s" dur="0.2s" values="M12 19v1M19 12h1M12 5v-1M5 12h-1;M12 21v1M21 12h1M12 3v-1M3 12h-1"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.5s" dur="0.2s" values="2;0"/></path><path d="M0 0"><animate fill="freeze" attributeName="d" begin="0.7s" dur="0.2s" values="M17 17l0.5 0.5M17 7l0.5 -0.5M7 7l-0.5 -0.5M7 17l-0.5 0.5;M18.5 18.5l0.5 0.5M18.5 5.5l0.5 -0.5M5.5 5.5l-0.5 -0.5M5.5 18.5l-0.5 0.5"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.7s" dur="0.2s" values="2;0"/></path><animateTransform attributeName="transform" dur="30s" repeatCount="indefinite" type="rotate" values="0 12 12;360 12 12"/></g></g></svg>`,
  dark: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Sunny-outline-to-moon-alt-loop-transition SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"><g stroke-dasharray="2"><path d="M12 21v1M21 12h1M12 3v-1M3 12h-1"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.2s" values="4;2"/></path><path d="M18.5 18.5l0.5 0.5M18.5 5.5l0.5 -0.5M5.5 5.5l-0.5 -0.5M5.5 18.5l-0.5 0.5"><animate fill="freeze" attributeName="stroke-dashoffset" begin="0.2s" dur="0.2s" values="4;2"/></path></g><path d="M7 6 C7 12.08 11.92 17 18 17 C18.53 17 19.05 16.96 19.56 16.89 C17.95 19.36 15.17 21 12 21 C7.03 21 3 16.97 3 12 C3 8.83 4.64 6.05 7.11 4.44 C7.04 4.95 7 5.47 7 6 Z" opacity="0"><set attributeName="opacity" begin="0.5s" to="1"/></path></g><g fill="none" stroke="currentColor" stroke-dasharray="4" stroke-dashoffset="4" stroke-linecap="round" stroke-linejoin="round"><path d="M13 4h1.5M13 4h-1.5M13 4v1.5M13 4v-1.5"><animate id="lineMdSunnyOutlineToMoonAltLoopTransition0" fill="freeze" attributeName="stroke-dashoffset" begin="0.6s;lineMdSunnyOutlineToMoonAltLoopTransition0.begin+6s" dur="0.4s" values="4;0"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="lineMdSunnyOutlineToMoonAltLoopTransition0.begin+2s;lineMdSunnyOutlineToMoonAltLoopTransition0.begin+4s" dur="0.4s" values="4;0"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="lineMdSunnyOutlineToMoonAltLoopTransition0.begin+1.2s;lineMdSunnyOutlineToMoonAltLoopTransition0.begin+3.2s;lineMdSunnyOutlineToMoonAltLoopTransition0.begin+5.2s" dur="0.4s" values="0;4"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition0.begin+1.8s" to="M12 5h1.5M12 5h-1.5M12 5v1.5M12 5v-1.5"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition0.begin+3.8s" to="M12 4h1.5M12 4h-1.5M12 4v1.5M12 4v-1.5"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition0.begin+5.8s" to="M13 4h1.5M13 4h-1.5M13 4v1.5M13 4v-1.5"/></path><path d="M19 11h1.5M19 11h-1.5M19 11v1.5M19 11v-1.5"><animate id="lineMdSunnyOutlineToMoonAltLoopTransition1" fill="freeze" attributeName="stroke-dashoffset" begin="1s;lineMdSunnyOutlineToMoonAltLoopTransition1.begin+6s" dur="0.4s" values="4;0"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="lineMdSunnyOutlineToMoonAltLoopTransition1.begin+2s;lineMdSunnyOutlineToMoonAltLoopTransition1.begin+4s" dur="0.4s" values="4;0"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="lineMdSunnyOutlineToMoonAltLoopTransition1.begin+1.2s;lineMdSunnyOutlineToMoonAltLoopTransition1.begin+3.2s;lineMdSunnyOutlineToMoonAltLoopTransition1.begin+5.2s" dur="0.4s" values="0;4"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition1.begin+1.8s" to="M17 11h1.5M17 11h-1.5M17 11v1.5M17 11v-1.5"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition1.begin+3.8s" to="M18 12h1.5M18 12h-1.5M18 12v1.5M18 12v-1.5"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition1.begin+5.8s" to="M19 11h1.5M19 11h-1.5M19 11v1.5M19 11v-1.5"/></path><path d="M19 4h1.5M19 4h-1.5M19 4v1.5M19 4v-1.5"><animate id="lineMdSunnyOutlineToMoonAltLoopTransition2" fill="freeze" attributeName="stroke-dashoffset" begin="2.8s;lineMdSunnyOutlineToMoonAltLoopTransition2.begin+6s" dur="0.4s" values="4;0"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="lineMdSunnyOutlineToMoonAltLoopTransition2.begin+2s" dur="0.4s" values="4;0"/><animate fill="freeze" attributeName="stroke-dashoffset" begin="lineMdSunnyOutlineToMoonAltLoopTransition2.begin+1.2s;lineMdSunnyOutlineToMoonAltLoopTransition2.begin+3.2s" dur="0.4s" values="0;4"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition2.begin+1.8s" to="M20 5h1.5M20 5h-1.5M20 5v1.5M20 5v-1.5"/><set attributeName="d" begin="lineMdSunnyOutlineToMoonAltLoopTransition2.begin+5.8s" to="M19 4h1.5M19 4h-1.5M19 4v1.5M19 4v-1.5"/></path></g><mask id="lineMdSunnyOutlineToMoonAltLoopTransition3"><circle cx="12" cy="12" r="12" fill="#fff"/><circle cx="12" cy="12" r="4"><animate fill="freeze" attributeName="r" begin="0.1s" dur="0.4s" values="4;8"/></circle><circle cx="22" cy="2" r="3" fill="#fff"><animate fill="freeze" attributeName="cx" begin="0.1s" dur="0.4s" values="22;18"/><animate fill="freeze" attributeName="cy" begin="0.1s" dur="0.4s" values="2;6"/><animate fill="freeze" attributeName="r" begin="0.1s" dur="0.4s" values="3;12"/></circle><circle cx="22" cy="2" r="1"><animate fill="freeze" attributeName="cx" begin="0.1s" dur="0.4s" values="22;18"/><animate fill="freeze" attributeName="cy" begin="0.1s" dur="0.4s" values="2;6"/><animate fill="freeze" attributeName="r" begin="0.1s" dur="0.4s" values="1;10"/></circle></mask><circle cx="12" cy="12" r="6" fill="currentColor" mask="url(#lineMdSunnyOutlineToMoonAltLoopTransition3)"><set attributeName="opacity" begin="0.5s" to="0"/><animate fill="freeze" attributeName="r" begin="0.1s" dur="0.4s" values="6;10"/></circle></svg>`,
  default: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Paint-drop SVG Icon</title><path fill="none" stroke="currentColor" stroke-dasharray="28" stroke-dashoffset="28" stroke-linecap="round" stroke-width="2" d="M12 3C12 3 19 9 19 15C19 17 18 21 12 21M12 3C12 3 5 9 5 15C5 17 6 21 12 21"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.4s" values="28;0"/></path></svg>`,
};
const edgeIcons = {
  round: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Emoji-smile SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2"><path stroke-dasharray="60" stroke-dashoffset="60" d="M12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="60;0"/></path><path stroke-dasharray="14" stroke-dashoffset="14" d="M8 14C8.5 15.5 9.79086 17 12 17C14.2091 17 15.5 15.5 16 14"><animate fill="freeze" attributeName="stroke-dashoffset" begin="1s" dur="0.2s" values="14;0"/></path></g><g fill="currentColor" fill-opacity="0"><ellipse cx="9" cy="9.5" rx="1" ry="1.5"><animate fill="freeze" attributeName="fill-opacity" begin="0.6s" dur="0.2s" values="0;1"/></ellipse><ellipse cx="15" cy="9.5" rx="1" ry="1.5"><animate fill="freeze" attributeName="fill-opacity" begin="0.8s" dur="0.2s" values="0;1"/></ellipse></g></svg>`,
  soft: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Emoji-neutral SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2"><path stroke-dasharray="60" stroke-dashoffset="60" d="M12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="60;0"/></path><path stroke-dasharray="10" stroke-dashoffset="10" d="M8 15H16"><animate fill="freeze" attributeName="stroke-dashoffset" begin="1s" dur="0.2s" values="10;0"/></path></g><g fill="currentColor" fill-opacity="0"><ellipse cx="9" cy="9.5" rx="1" ry="1.5"><animate fill="freeze" attributeName="fill-opacity" begin="0.6s" dur="0.2s" values="0;1"/></ellipse><ellipse cx="15" cy="9.5" rx="1" ry="1.5"><animate fill="freeze" attributeName="fill-opacity" begin="0.8s" dur="0.2s" values="0;1"/></ellipse></g></svg>`,
  rough: `<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><title>Emoji-frown SVG Icon</title><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-width="2"><path stroke-dasharray="60" stroke-dashoffset="60" d="M12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3Z"><animate fill="freeze" attributeName="stroke-dashoffset" dur="0.5s" values="60;0"/></path><path stroke-dasharray="14" stroke-dashoffset="14" d="M8 16C8.5 15 9.79086 14 12 14C14.2091 14 15.5 15 16 16"><animate fill="freeze" attributeName="stroke-dashoffset" begin="1s" dur="0.2s" values="14;0"/></path></g><g fill="currentColor" fill-opacity="0"><ellipse cx="9" cy="9.5" rx="1" ry="1.5"><animate fill="freeze" attributeName="fill-opacity" begin="0.6s" dur="0.2s" values="0;1"/></ellipse><ellipse cx="15" cy="9.5" rx="1" ry="1.5"><animate fill="freeze" attributeName="fill-opacity" begin="0.8s" dur="0.2s" values="0;1"/></ellipse></g></svg>`,
};

const i18n = {
  ru: {
    authTitle: 'Функции ООП — вход',
    authSubtitle: 'Работа с табулированными функциями через /api',
    login: 'Вход',
    register: 'Регистрация',
    nickname: 'Ник',
    password: 'Пароль',
    email: 'Email',
    repeatPassword: 'Повтор пароля',
    loginAction: 'Войти',
    registerAction: 'Зарегистрироваться',
    headerTitle: 'Функции ООП — веб-интерфейс',
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
    modalCompositeTitle: 'Создание сложной функции',
    firstFn: 'Первая функция (f)',
    secondFn: 'Вторая функция (g)',
    save: 'Сохранить',
    settingsTitle: 'Настройки',
    theme: 'Тема',
    edges: 'Скругление',
    language: 'Язык',
    inclusive: 'Супер-контраст и крупный текст',
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
      round: 'Круглые края',
      soft: 'Мягкие края',
      rough: 'Прямые края',
    },
    error: 'Ошибка',
    ok: 'Понятно',
    footerLinks: ['Документация','Поддержка','Политика','Контакты','О проекте','Помощь','API','Скачать'],
    langRu: 'Русский',
    langEn: 'English',
    delete: 'Удалить',
    open: 'Открыть',
    profile: 'Профиль',
    update: 'Обновить',
    deleteAccount: 'Удалить аккаунт',
    passwordNew: 'Новый пароль',
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
    authTitle: 'OOP Functions — Sign in',
    authSubtitle: 'Work with tabulated functions via /api',
    login: 'Login',
    register: 'Register',
    nickname: 'Username',
    password: 'Password',
    email: 'Email',
    repeatPassword: 'Repeat password',
    loginAction: 'Sign in',
    registerAction: 'Sign up',
    headerTitle: 'OOP Functions — Web UI',
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
    modalCompositeTitle: 'Create composite function',
    firstFn: 'First function (f)',
    secondFn: 'Second function (g)',
    save: 'Save',
    settingsTitle: 'Settings',
    theme: 'Theme',
    edges: 'Corner style',
    language: 'Language',
    inclusive: 'High contrast & large text',
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
      round: 'Round edges',
      soft: 'Soft edges',
      rough: 'Sharp edges',
    },
    error: 'Error',
    ok: 'Got it',
    footerLinks: ['Docs','Support','Policy','Contacts','About','Help','API','Download'],
    langRu: 'Russian',
    langEn: 'English',
    delete: 'Delete',
    open: 'Open',
    profile: 'Profile',
    update: 'Update',
    deleteAccount: 'Delete account',
    passwordNew: 'New password',
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

async function fetchPointsNormalized(functionId, depth = 0) {
  if (depth > 3) return [];
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

  try {
    const fnRes = await api.get(`/functions/${functionId}`);
    const fnType = (fnRes?.functionType || fnRes?.function_type || '').toUpperCase();
    if (fnType === 'COMPOSITE' && points.length) {
      return dedupeByX(points);
    }
    if (fnType === 'COMPOSITE' && !points.length) {
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
      }
      if (firstId && secondId) {
        const composed = await composeFunctions(firstId, secondId, depth + 1);
        if (composed?.length) return dedupeByX(composed);
      }
    }
    points = dedupeByX(points);
    if (!points.length && fnType !== 'TABULATED' && fnType !== 'COMPOSITE') {
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
  } catch (e) {
    console.warn('fetchPointsNormalized fallback failed', e);
  }
    try {
    return dedupeByX(points);
  } catch {
    return points;
  }
}

async function composeFunctions(firstId, secondId, depth = 0) {
  const fPts = (await fetchPointsNormalized(Number(firstId), depth)).map((p) => ({
    ...p,
    xValue: Number(p.xValue),
    yValue: Number(p.yValue),
  })).filter((p) => Number.isFinite(p.xValue) && Number.isFinite(p.yValue));
  const gPts = (await fetchPointsNormalized(Number(secondId), depth)).map((p) => ({
    ...p,
    xValue: Number(p.xValue),
    yValue: Number(p.yValue),
  })).filter((p) => Number.isFinite(p.xValue) && Number.isFinite(p.yValue));
  console.log('compose source fPts', fPts, 'gPts', gPts);
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
    const samples = Math.max(200, inner.length * 8);
    const step = (innerMax - innerMin) / (samples - 1 || 1);
    const pts = [];
    for (let i = 0; i < samples; i++) {
      const x = innerMin + i * step;
      const yInner = linearInterpolate(innerSorted, x);
      if (yInner === null || yInner === undefined) continue;
      if (yInner < outerMin || yInner > outerMax) continue;
      const yOuter = linearInterpolate(outerSorted, yInner);
      if (yOuter === null || yOuter === undefined) continue;
      pts.push({ pointId: `comp-${pts.length}`, functionId: `comp-${firstId}-${secondId}`, xValue: x, yValue: yOuter });
    }
    console.log(`[compose ${label}] inner x:[${innerMin},${innerMax}] outer x:[${outerMin},${outerMax}] samples=${samples} result=${pts.length}`);
    return pts;
  };

  const firstOrder = build(fPts, gPts, 'g(f(x))');
  if (firstOrder.length) return firstOrder;
  const secondOrder = build(gPts, fPts, 'f(g(x))');
  return secondOrder;
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
      };
    },
    mounted() {
      this.initFromStorage();
      this.runStageAnimation();
    },
    methods: {
      resetState() {
        const fresh = createInitialState();
        Object.keys(fresh).forEach((key) => {
          this.state[key] = fresh[key];
        });
        this.stageAnimated = false;
      },
      toast(message, type = 'success') {
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
        const detail = (lang === 'en' ? detailsEn[type] : detailsRu[type]) || (lang === 'en' ? detailsEn.info : detailsRu.info);
        const msg = message
          ? `${message}${/[.!?]$/.test(message.trim()) ? '' : '.'} ${detail}`
          : detail;
        const icons = {
          success: 'https://i.pinimg.com/736x/d5/09/ca/d509ca41d6d01f406cb13ab9dbd06178.jpg',
          warning: 'https://i.pinimg.com/1200x/21/36/2e/21362e6ec4312730016613384f25f41c.jpg',
          error: 'https://i.pinimg.com/1200x/21/36/2e/21362e6ec4312730016613384f25f41c.jpg',
          info: 'https://i.pinimg.com/736x/9f/e4/be/9fe4be23d357519d0ce35bd93137b1bb.jpg',
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
            throw new Error('Заполните логин и пароль');
          }
          if (this.state.authForm.username.length > 32) {
            throw new Error(this.state.ui.lang === 'en'
              ? 'Username must be at most 32 characters.'
              : 'Логин не длиннее 32 символов.');
          }
          if (this.state.authForm.email && this.state.authForm.email.length > 128) {
            throw new Error(this.state.ui.lang === 'en'
              ? 'Email must be at most 128 characters.'
              : 'Email не длиннее 128 символов.');
          }
          if (this.state.authForm.password.length > 128) {
            throw new Error(this.state.ui.lang === 'en'
              ? 'Password must be at most 128 characters.'
              : 'Пароль не длиннее 128 символов.');
          }
          if (this.state.authForm.password !== this.state.authForm.confirm) {
            throw new Error('Пароли не совпадают');
          }
          this.state.loading = true;
          const res = await api.post('/auth/register', {
            username: this.state.authForm.username,
            password: this.state.authForm.password,
            email: this.state.authForm.email,
          });
          this.toast('Регистрация успешна, войдите под новым пользователем');
          this.state.authMode = 'login';
          this.state.credentials.username = this.state.authForm.username;
          this.state.credentials.password = this.state.authForm.password;
          this.state.credentials.email = this.state.authForm.email;
          if (res?.userId) {
            this.state.credentials.userId = res.userId;
          }
        } catch (e) {
          const msg = e.message || (this.state.ui.lang === 'en'
            ? 'Registration error: check login/email/password.'
            : 'Ошибка регистрации: проверьте логин/email/пароль.');
          this.showError(this.t('register'), msg);
        } finally {
          this.state.loading = false;
        }
      },
      async tryLogin() {
        try {
          const { username, password } = this.state.credentials;
          if (!username || !password) throw new Error('Введите логин и пароль');
          this.state.loading = true;
          api.setAuth(username, password);
          await this.loadCurrentUser();
          await api.get('/functions');
          this.state.isAuthed = true;
          persistCredentials(this.state.credentials);
          await this.refreshFunctions();
          this.toast('Готово! Соединение установлено');
        } catch (e) {
          api.clearAuth();
          let msg = e.message || (this.state.ui.lang === 'en' ? 'Failed to login' : 'Не удалось войти');
          if (e.message?.includes('Unauthorized')) {
            msg = this.state.ui.lang === 'en' ? 'Wrong login or password' : 'Неверный логин или пароль';
          }
          this.showError(this.t('login'), msg);
        } finally {
          this.state.loading = false;
        }
      },
      logout() {
        api.clearAuth();
        localStorage.removeItem(STORAGE_KEY);
        chart.destroy();
        this.resetState();
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
      async refreshFunctions() {
        try {
          this.state.loadingGlobal = true;
          const [fnData, compData] = await Promise.all([
            api.get('/functions'),
            api.get('/composite-functions').catch(() => []),
          ]);
          const baseFunctions = (fnData || []).map((f) => normalizeFunction(f));
          const composites = (compData || []).map((c) => {
            const id = c.compositeId ?? c.id;
            return normalizeFunction({
              ...c,
              compositeId: id,
              functionId: `comp-${id}`,
              functionName: c.compositeName ?? c.name ?? 'Composite',
              functionType: 'COMPOSITE',
              firstFunctionId: c.firstFunctionId ?? c.first_function_id ?? c.first_functionId,
              secondFunctionId: c.secondFunctionId ?? c.second_function_id ?? c.second_functionId,
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
          this.showError('Загрузка функций', e.message);
        } finally {
          this.state.loadingGlobal = false;
          this.runStageAnimation();
        }
      },
      async deleteFunction(id) {
        try {
          await api.delete(`/functions/${id}`);
          this.toast('Функция удалена', 'warning');
          await this.refreshFunctions();
          if (this.state.selectedFunction?.functionId === id) {
            this.state.selectedFunction = null;
            this.state.points = [];
            chart.destroy();
          }
        } catch (e) {
          this.showError('Удаление функции', e.message);
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
              if (!firstId || !secondId) {
                try {
                  const compMeta = await api.get(`/composite-functions/${compId || functionId}/points`);
                  firstId = firstId || compMeta?.firstFunctionId || compMeta?.first_function_id;
                  secondId = secondId || compMeta?.secondFunctionId || compMeta?.second_function_id;
                } catch (err) {
                  console.warn('composite meta fetch failed', err);
                }
              }
              if (!firstId || !secondId) {
                this.toast('Не удалось получить дочерние функции для композиции', 'warning');
                chart.destroy();
                this.state.points = [];
                return;
              }
              points = await composeFunctions(firstId, secondId);
              if (!points.length) {
                this.toast('Композиция вне области определения: скорректируйте диапазоны', 'warning');
                chart.destroy();
                this.state.points = [];
                return;
              }
            } catch (err) {
              this.toast('Не удалось вычислить композицию', 'warning');
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
                this.toast('Для функции нет точек. Укажите xFrom/xTo или создайте точки.', 'warning');
              } else {
                points = synthesizePointsFromFunction(this.state.selectedFunction);
                if (!points.length) {
                  this.toast('Для функции нет точек. Укажите xFrom/xTo или создайте точки.', 'warning');
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
          this.showError('Точки функции', e.message);
        }
      },
      async updatePoint(point) {
        try {
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
            this.toast('Точка обновлена');
          } else if (this.state.selectedFunction?.functionId) {
            await api.post(`/functions/${this.state.selectedFunction.functionId}/points`, {
              xValue: x,
              yValue: y,
            });
            this.toast('Точка сохранена');
          } else {
            this.toast('Нет выбранной функции для сохранения точки', 'warning');
            return;
          }
          await this.loadPoints(point.functionId || this.state.selectedFunction?.functionId);
        } catch (e) {
          this.showError('Обновление точки', e.message);
        }
      },
      async deletePoint(point) {
        try {
          const isPersisted = Number.isFinite(Number(point.pointId));
          if (isPersisted) {
            await api.delete(`/points/${point.pointId}`);
            this.toast('Точка удалена', 'warning');
          } else {
            this.state.points = this.state.points.filter((p) => p.pointId !== point.pointId);
            this.toast('Локальная точка удалена', 'warning');
          }
          await this.loadPoints(point.functionId || this.state.selectedFunction?.functionId);
        } catch (e) {
          this.showError('Удаление точки', e.message);
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
        if (!points || points.length < 2) {
          throw new Error('Нужно минимум 2 точки');
        }

        const payload = {
          userId: Number(this.state.credentials.userId),
          functionName: name || 'f(x)',
          functionType: type || 'TABULATED',
          xFrom: xFrom ?? null,
          xTo: xTo ?? null,
        };

        const fn = await api.post('/functions', payload);
        for (const p of points) {
          await api.post(`/functions/${fn.functionId}/points`, {
            xValue: Number(p.xValue ?? p.x),
            yValue: Number(p.yValue ?? p.y),
          });
        }
        this.state.points = points.map((p, idx) => ({
          pointId: `local-${idx}`,
          functionId: fn.functionId,
          xValue: Number(p.xValue ?? p.x),
          yValue: Number(p.yValue ?? p.y),
        }));
        return fn;
      },
      async createFromTable() {
        try {
          const points = this.state.createForm.points.map((p) => ({
            xValue: Number(p.x),
            yValue: Number(p.y),
          }));
          const fn = await this.createFunctionWithPoints({
            name: this.state.createForm.name,
            type: this.state.createForm.type,
            xFrom: this.state.createForm.xFrom,
            xTo: this.state.createForm.xTo,
            points,
          });
          this.toast('Функция сохранена');
          this.state.modals.create = false;
          await this.refreshFunctions();
          await this.loadPoints(fn.functionId);
          this.state.createForm = createInitialState().createForm;
        } catch (e) {
          this.showError('Создание функции', e.message);
        }
      },
      async createFromMath() {
        try {
          const { xFrom, xTo, pointsCount, mathKey, constant, name } = this.state.mathForm;
          if (pointsCount < 2) throw new Error('Минимум 2 точки');
          const label = (mathFunctions.find((m) => m.key === mathKey)?.label) || 'f(x)';
          const finalName = name && name.trim() ? name : label;
          const step = (xTo - xFrom) / (pointsCount - 1);
          const evalFn = mathEvaluators[mathKey];
          const points = Array.from({ length: pointsCount }, (_, idx) => {
            const x = xFrom + idx * step;
            const y = evalFn ? evalFn(x, constant) : 0;
            return { xValue: x, yValue: y };
          });
          const fn = await this.createFunctionWithPoints({
            name: finalName,
            type: 'MATH',
            xFrom,
            xTo,
            points,
          });
          this.toast('Функция по формуле создана');
          this.state.modals.math = false;
          await this.refreshFunctions();
          await this.loadPoints(fn.functionId);
          this.state.mathForm = createInitialState().mathForm;
        } catch (e) {
          this.showError('Создание по формуле', e.message);
        }
      },
      async saveComposite() {
        try {
          if (!this.state.credentials.userId) throw new Error('Укажите userId');
          const { name, a, b } = this.state.compositeForm;
          if (!name || !a || !b) throw new Error('Заполните имя и выберите обе функции');
          await api.post('/composite-functions', {
            userId: Number(this.state.credentials.userId),
            compositeName: name,
            firstFunctionId: a,
            secondFunctionId: b,
          });
          this.toast('Композит сохранён');
          this.state.compositeForm = createInitialState().compositeForm;
          this.state.modals.composite = false;
          await this.refreshFunctions();
        } catch (e) {
          this.showError('Композитная функция', e.message);
        }
      },
      addEmptyPointRow() {
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
        this.scheduleApplyTheme();
        this.state.modals.settings = false;
        this.toast('Настройки сохранены');
      },
  async openProfile() {
    try {
      this.state.loadingGlobal = true;
      const me = await this.loadCurrentUser(this.state.credentials.username);
      this.state.profileForm.username = me?.username || this.state.credentials.username || '';
      this.state.profileForm.email = me?.email || this.state.credentials.email || this.state.authForm.email || '';
      this.state.profileForm.password = '';
      this.state.modals.profile = true;
    } catch (e) {
      this.showError('Профиль', e.message || 'Не удалось загрузить профиль');
    } finally {
      this.state.loadingGlobal = false;
    }
  },
  async saveProfile() {
    try {
      if (!this.state.credentials.userId) {
            await this.loadCurrentUser();
          }
          if (!this.state.credentials.userId) {
            this.showError(this.t('profile'), this.state.ui.lang === 'en'
              ? 'User id is unknown. Re-login, please.'
              : 'UserId не определён. Перелогиньтесь.');
            return;
      }
      if (!/^[A-Za-z0-9_-]{3,32}$/.test(this.state.profileForm.username)) {
        throw new Error(this.state.ui.lang === 'en'
          ? 'Username must be 3-32 chars, letters/digits/_/- only.'
          : 'Логин 3-32 символа, только буквы/цифры/_/-');
      }
      if (this.state.profileForm.email && !/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(this.state.profileForm.email)) {
        throw new Error(this.state.ui.lang === 'en'
          ? 'Enter a valid email.'
          : 'Введите корректный email.');
      }
      if (this.state.profileForm.password && this.state.profileForm.password.length < 6) {
        throw new Error(this.state.ui.lang === 'en'
          ? 'Password must be at least 6 characters.'
          : 'Пароль должен быть не короче 6 символов.');
      }
      if (this.state.profileForm.password) {
        const pwd = this.state.profileForm.password;
        const strong = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{6,128}$/;
        if (!strong.test(pwd)) {
          throw new Error(this.state.ui.lang === 'en'
            ? 'Password must have upper, lower, digit and special char (6-128).'
            : 'Пароль должен содержать прописные, строчные, цифру и спецсимвол (6-128).');
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
        msg = this.state.ui.lang === 'en' ? 'This username is already taken.' : 'Такой логин уже занят.';
      } else {
        msg = e.message || this.t('error');
      }
      this.showError('Профиль', msg);
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
      this.showError('Удаление', msg);
    }
  },
      async saveAllPoints() {
        if (!this.state.selectedFunction || !this.state.points.length) return;
        try {
          const fnId = this.state.selectedFunction.functionId;
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
        this.toast('Экспортировано в JSON');
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
          this.toast('Точки загружены в форму создания');
        } catch (e) {
          this.showError('Импорт', e.message);
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
      },
      'state.ui.edges'(val, old) {
        if (val === old) return;
        localStorage.setItem('ui-edges', this.state.ui.edges || 'soft');
        this.scheduleApplyTheme();
      },
      'state.ui.lang'() {
        localStorage.setItem('ui-lang', this.state.ui.lang);
      },
    },
    created() {
      this.applyTheme();
    },
  }).mount('#app');
})();

