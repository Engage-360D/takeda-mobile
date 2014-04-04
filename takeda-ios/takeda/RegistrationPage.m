//
//  RegistrationPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "RegistrationPage.h"
#import <QuartzCore/QuartzCore.h>



@interface RegistrationPage (){
    UIActionSheet *picker_cover;
    NSMutableArray *regions_data;
    UIPickerView *region_picker;
    UIDatePicker *date_picker;
}

@end

@implementation RegistrationPage{
    
}
@synthesize scrollView;
@synthesize btn_register;
@synthesize bg_block;
@synthesize name_field;
@synthesize email_field;
@synthesize pass_field;

@synthesize user_is_doctor;
@synthesize user_is_agree_personal_data;
@synthesize user_is_agree_email_subscribe_data;
@synthesize user_is_agree_information_is_recomemd_style;

@synthesize user_is_doctor_img;
@synthesize user_is_agree_personal_data_img;
@synthesize user_is_agree_email_subscribe_data_img;
@synthesize user_is_agree_information_is_recomemd_style_img;


@synthesize btn_birthday;
@synthesize btn_region;

int sel_index_region = 0;





- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        [self setDefoultData];
    }
    return self;
}


-(void)setDefoultData{
    self.user_is_doctor = NO;
    self.user_is_agree_personal_data = NO;
    self.user_is_agree_email_subscribe_data = NO;
    self.user_is_agree_information_is_recomemd_style = NO;
    
    regions_data = [[NSMutableArray alloc] init];
    [regions_data addObject:@"Российская Федерация"];
    [regions_data addObject:@"Украина"];
    [regions_data addObject:@"Польша"];
    [regions_data addObject:@"Словакия"];
    [regions_data addObject:@"Франция"];
}




-(void)setFieldsSettings{
    for (UIView *view in self.bg_block) {
        CALayer *TopBorder = [CALayer layer];
        TopBorder.frame = CGRectMake(0.0f, 0.0f, view.frame.size.width, 1.0f);
        TopBorder.contents = (id)[UIImage imageNamed:@"bg_separator"].CGImage;
        [view.layer addSublayer:TopBorder];
        CALayer *BottomBorder = [CALayer layer];
        BottomBorder.frame = CGRectMake(0.0f, view.frame.size.height, view.frame.size.width, 1.0f);
        BottomBorder.contents = (id)[UIImage imageNamed:@"bg_separator"].CGImage;
        [view.layer addSublayer:BottomBorder];
    }
    
    [self.btn_register setBackgroundImage:[[UIImage imageNamed:@"button_arrow_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 30)] forState:UIControlStateNormal];
    
    self.email_field.placeholderColor = RGB(53, 65, 71);
    self.email_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    self.pass_field.placeholderColor = RGB(53, 65, 71);
    self.pass_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    self.name_field.placeholderColor = RGB(53, 65, 71);
    self.name_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    self.title = @"";
    [self setNavImage];
    [self setNavigationPanel];
    [self setFieldsSettings];
    

    self.scrollView.frame = RectSetOrigin(self.view.frame, 0, 0);
    self.scrollView.frame = CGRectMake(0, self.btn_register.frame.size.height+2, 320, self.view.frame.size.height - self.btn_register.frame.size.height);
    [self.view addSubview:self.scrollView];
    [self.scrollView setContentSize:CGSizeMake(320, 670)];

}



-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self updateFields];
}


-(void)goBack{
    [self.navigationController popViewControllerAnimated:YES];
}




#pragma mark - navigation panel
-(void)setNavImage{
    UINavigationBar *navBar = [[self navigationController] navigationBar];
    UIImage *backgroundImage;
    backgroundImage = [[UIImage imageNamed:@"nav_bar_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 10)] ;
    [navBar setBackgroundImage:backgroundImage forBarMetrics:UIBarMetricsDefault];
}

-(void)setNavigationPanel{
    UIButton *back_btn = [UIButton buttonWithType:UIButtonTypeCustom];
    back_btn.frame = CGRectMake(0, 0, 80, 40);
    back_btn.titleLabel.font = [UIFont fontWithName:@"Helvetica-Light" size:17.0];
    [back_btn addTarget:self action:@selector(goBack) forControlEvents:UIControlEventTouchDown];
    
    [back_btn setTitle:@"Назад" forState:UIControlStateNormal];
    CALayer *back_arrow = [CALayer layer];
    back_arrow.frame = CGRectMake(0.0f, back_btn.frame.size.height/2 - 7.5, 8, 15.0f);
    back_arrow.contents = (id)[UIImage imageNamed:@"left_white_arrow"].CGImage;
    [back_btn.layer addSublayer:back_arrow];
    UIBarButtonItem *back_item = [[UIBarButtonItem alloc] initWithCustomView:back_btn];
    self.navigationItem.leftBarButtonItem = back_item;
    
    
    
    UIImage *logoImage = [UIImage imageNamed:@"title_logo"];
    UIImageView *img_logo = [[UIImageView alloc] initWithFrame:CGRectMake(40, 8, logoImage.size.width, logoImage.size.height)];
    img_logo.image = logoImage;
    self.navigationItem.titleView = img_logo;
}

#pragma mark -






-(void)updateFields{
    if (self.user_is_doctor) {
        self.user_is_doctor_img.image = [UIImage imageNamed:@"checked_item"];
    }else{
        self.user_is_doctor_img.image = [UIImage imageNamed:@"unchecked_item"];
    }
    
    if (self.user_is_agree_personal_data) {
        self.user_is_agree_personal_data_img.image = [UIImage imageNamed:@"checked_item"];
    }else{
        self.user_is_agree_personal_data_img.image = [UIImage imageNamed:@"unchecked_item"];
    }
    
    if (self.user_is_agree_email_subscribe_data) {
        self.user_is_agree_email_subscribe_data_img.image = [UIImage imageNamed:@"checked_item"];
    }else{
        self.user_is_agree_email_subscribe_data_img.image = [UIImage imageNamed:@"unchecked_item"];
    }
    
    if (self.user_is_agree_information_is_recomemd_style) {
        self.user_is_agree_information_is_recomemd_style_img.image = [UIImage imageNamed:@"checked_item"];
    }else{
        self.user_is_agree_information_is_recomemd_style_img.image = [UIImage imageNamed:@"unchecked_item"];
    }
}





-(IBAction)selectField:(id)sender{
    switch ((int)[sender tag]) {
        case 1:{
            self.user_is_doctor = !self.user_is_doctor;
            break;}
        case 2:{
            self.user_is_agree_personal_data = !self.user_is_agree_personal_data;
            break;}
        case 3:{
            self.user_is_agree_email_subscribe_data = !self.user_is_agree_email_subscribe_data;
            break;}
        case 4:{
            self.user_is_agree_information_is_recomemd_style = !self.user_is_agree_information_is_recomemd_style;
            break;}
        default:
            break;
    }
    [self updateFields];
    
}




#pragma mark - UITextFieldDelegate

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}
#pragma mark -




#pragma mark - UIPickerView

-(IBAction)showPickerRegion:(id)sender{
    picker_cover = nil;
    picker_cover = [[UIActionSheet alloc] initWithTitle:nil
                                       delegate:nil
                              cancelButtonTitle:nil
                         destructiveButtonTitle:nil
                              otherButtonTitles:nil];
    
    UIToolbar *toolbar = [[UIToolbar alloc] init];
    toolbar.frame = CGRectMake(0, 0, ScreenWidth, 44);
    NSMutableArray *items = [[NSMutableArray alloc] init];
    
    UIBarButtonItem *button1 = [[UIBarButtonItem alloc] initWithTitle:@"Закрыть" style:UIBarButtonItemStyleDone target:self action:@selector(closePicker)];
    
    UIBarButtonItem *button2 = [[UIBarButtonItem alloc] initWithTitle:@"Применить" style:UIBarButtonItemStyleDone target:self action:@selector(applyPicker:)];
    
    UIBarButtonItem *flexibleSpaceLeft = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    
    [items addObject:button1];
    [items addObject:flexibleSpaceLeft];
    [items addObject:button2];
    [toolbar setItems:items animated:NO];
    
    float picker_width = ScreenWidth;
    region_picker = [[UIPickerView alloc] initWithFrame:CGRectMake(0,40,picker_width,210)];
    region_picker.delegate = self;
    region_picker.dataSource = self;
    region_picker.showsSelectionIndicator = YES;
    [picker_cover addSubview:toolbar];
    [picker_cover addSubview:region_picker];
    
    [picker_cover showInView:self.view.superview];
    [picker_cover setBounds:CGRectMake(0,0,ScreenWidth,390)];
    picker_cover.tag = 1;
    
}

-(void)closePicker{
    [picker_cover dismissWithClickedButtonIndex:0 animated:YES];
}


-(void)applyPicker:(id)sender{
    if ((int)[picker_cover tag]==1) {
        [self.btn_region.titleLabel setText:[NSString stringWithFormat:@"Страна: %@",[regions_data objectAtIndex:sel_index_region]]];
    }else{
        if ((int)[picker_cover tag]==2) {
            NSDate *myDate = date_picker.date;
            NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];
            [dateFormat setDateFormat:@"dd MMMM yyyy"];
            NSString *prettyVersion = [dateFormat stringFromDate:myDate];
            [self.btn_birthday setTitle:[NSString stringWithFormat:@"Дата рождения: %@",prettyVersion] forState:UIControlStateNormal];
        }
    }
    [picker_cover dismissWithClickedButtonIndex:0 animated:YES];
}


#pragma mark PickerView DataSource

- (NSInteger)numberOfComponentsInPickerView:
(UIPickerView *)pickerView
{
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView
numberOfRowsInComponent:(NSInteger)component
{
    return [regions_data count];
}

- (NSString *)pickerView:(UIPickerView *)pickerView
             titleForRow:(NSInteger)row
            forComponent:(NSInteger)component
{
    return [regions_data objectAtIndex:row];
}

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row
      inComponent:(NSInteger)component{
    sel_index_region = row;
    NSLog(@"index = %i text = %@",row,[regions_data objectAtIndex:row]);
}



#pragma mark -


#pragma mark - UIDatePicker

-(IBAction)showTimePicker:(id)sender{
    picker_cover = nil;
    picker_cover = [[UIActionSheet alloc] initWithTitle:nil
                                               delegate:nil
                                      cancelButtonTitle:nil
                                 destructiveButtonTitle:nil
                                      otherButtonTitles:nil];
    
    UIToolbar *toolbar = [[UIToolbar alloc] init];
    toolbar.frame = CGRectMake(0, 0, ScreenWidth, 44);
    NSMutableArray *items = [[NSMutableArray alloc] init];
    
    UIBarButtonItem *button1 = [[UIBarButtonItem alloc] initWithTitle:@"Закрыть" style:UIBarButtonItemStyleDone target:self action:@selector(closePicker)];
    
    UIBarButtonItem *button2 = [[UIBarButtonItem alloc] initWithTitle:@"Применить" style:UIBarButtonItemStyleDone target:self action:@selector(applyPicker:)];
    
    UIBarButtonItem *flexibleSpaceLeft = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    
    [items addObject:button1];
    [items addObject:flexibleSpaceLeft];
    [items addObject:button2];
    [toolbar setItems:items animated:NO];
    
    float picker_width = ScreenWidth;
    
    date_picker = [[UIDatePicker alloc] initWithFrame:CGRectMake(0,40,picker_width,210)];
    date_picker.datePickerMode = UIDatePickerModeDate;
    //[date_picker addTarget:self action:@selector(changeDate) forControlEvents:UIControlEventValueChanged];
    
    
    [picker_cover addSubview:toolbar];
    [picker_cover addSubview:date_picker];
    
    [picker_cover showInView:self.view.superview];
    [picker_cover setBounds:CGRectMake(0,0,ScreenWidth,390)];
    picker_cover.tag = 2;
}


#pragma mark -





@end
